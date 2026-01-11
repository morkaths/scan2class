package com.morkath.scan2class.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.dto.AttendanceUpdateDto;
import com.morkath.scan2class.dto.ClassroomStatsDTO;
import com.morkath.scan2class.dto.SessionStatsDTO;
import com.morkath.scan2class.dto.StudentStatDTO;
import com.morkath.scan2class.entity.attendance.AttendanceRecordEntity;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.entity.classroom.ClassParticipantEntity;
import com.morkath.scan2class.repository.attendance.AttendanceRecordRepository;
import com.morkath.scan2class.repository.attendance.SessionRepository;
import com.morkath.scan2class.repository.classroom.ClassParticipantRepository;
import com.morkath.scan2class.service.AttendanceService;

@Service
@Transactional
public class AttendanceServiceImpl extends BaseServiceImpl<AttendanceRecordEntity, Long>
        implements AttendanceService {

    private final SessionRepository sessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final com.morkath.scan2class.repository.auth.UserRepository userRepository;
    private final ClassParticipantRepository classParticipantRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${demo.mode.skip-location:false}")
    private boolean skipLocationCheck;

    @Autowired
    public AttendanceServiceImpl(AttendanceRecordRepository attendanceRecordRepository,
            SessionRepository sessionRepository,
            com.morkath.scan2class.repository.auth.UserRepository userRepository,
            ClassParticipantRepository classParticipantRepository,
            SimpMessagingTemplate messagingTemplate) {
        super(attendanceRecordRepository);
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.classParticipantRepository = classParticipantRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void attend(UserEntity user, String token, Double lat, Double lon, Double accuracy, String deviceInfo)
            throws Exception {
        // 1. Find Session by Token
        SessionEntity session = sessionRepository.findByToken(token);
        if (session == null) {
            throw new Exception("Phiên điểm danh không tồn tại hoặc token không hợp lệ.");
        }

        // 2. Validate Session Status
        if (!session.getIsOpen()) {
            throw new Exception("Phiên điểm danh đã kết thúc hoặc bị đóng.");
        }

        // 3. Validate Token Expiry
        if (!session.isTokenValid()) {
            throw new Exception("Mã QR đã hết hạn.");
        }

        // 4. Validate Student in Class
        boolean isParticipant = session.getClassroom().getParticipants().stream()
                .anyMatch(p -> p.getUser().getId().equals(user.getId()));
        if (!isParticipant) {
            throw new Exception("Bạn không phải là thành viên của lớp học này.");
        }

        // 5. Check Duplicate
        boolean alreadyAttended = attendanceRecordRepository.existsBySessionIdAndUserId(session.getId(), user.getId());
        if (alreadyAttended) {
            throw new Exception("Bạn đã điểm danh thành công trước đó rồi.");
        }

        // 6. Geofencing check
        if (!skipLocationCheck) {
            if (lat == null || lon == null) {
                throw new Exception("Không thể xác định vị trí của bạn.");
            }

            double dist = session.calculateDistance(lat, lon);
            double allowedRadius = session.getRadius();
            if (accuracy != null && accuracy > 0) {
                allowedRadius += (accuracy / 2);
            }

            if (dist > allowedRadius) {
                throw new Exception(String.format(
                        "Bạn đang ở quá xa vị trí lớp học (%.2fm). Bán kính cho phép: %.2fm (đã cộng sai số %.2fm).",
                        dist, allowedRadius, (accuracy != null ? accuracy / 2 : 0.0)));
            }
        }

        // 7. Save Record
        AttendanceRecordEntity record = new AttendanceRecordEntity();
        record.setSession(session);
        record.setUser(user);
        record.setCheckin(LocalDateTime.now());
        record.setLatitude(lat);
        record.setLongitude(lon);
        record.setDeviceInfo(deviceInfo);
        record.setDeviceUid("BROWSER");
        record.setStatus("PRESENT");
        save(record);

        // Broadcast update
        messagingTemplate.convertAndSend("/topic/sessions/" + session.getId() + "/attendance",
                new AttendanceUpdateDto(session.getId(), user.getId(), user.getUsername(), user.getFullname(),
                        record.getCheckin(), record.getStatus(), record.getDeviceInfo()));
    }

    @Override
    public void updateAttendanceStatus(Long userId, Long sessionId, String status, String note, UserEntity teacher)
            throws Exception {
        AttendanceRecordEntity record = attendanceRecordRepository.findBySessionIdAndUserId(sessionId, userId);

        if (record == null) {
            // Create new record
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("Sinh viên không tồn tại."));
            SessionEntity session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new Exception("Phiên điểm danh không tồn tại."));

            record = new AttendanceRecordEntity(session, user);
            record.setCheckin(LocalDateTime.now());
            record.setDeviceUid("MANUAL");
            record.setDeviceInfo("Manual Update by Teacher");
        }

        // Verify Teacher Ownership
        if (!record.getSession().getClassroom().getOwner().getId().equals(teacher.getId())) {
            throw new Exception("Bạn không có quyền sửa đổi điểm danh của lớp này.");
        }

        record.setStatus(status);
        save(record);

        // Broadcast update
        UserEntity student = record.getUser();
        messagingTemplate.convertAndSend("/topic/sessions/" + sessionId + "/attendance",
                new AttendanceUpdateDto(sessionId, student.getId(), student.getUsername(),
                        student.getFullname(), record.getCheckin(), record.getStatus(),
                        record.getDeviceInfo()));
    }

    @Override
    public ClassroomStatsDTO getClassroomStatistics(Long classroomId) {
        ClassroomStatsDTO stats = new ClassroomStatsDTO();
        stats.setClassroomId(classroomId);

        // 1. Get total sessions count
        int totalSessions = sessionRepository.countByClassroomId(classroomId);
        stats.setTotalSessions(totalSessions);

        // 2. Get all participants
        List<ClassParticipantEntity> participants = classParticipantRepository.findByIdClassId(classroomId);

        // 3. Get all attendance records for this class
        List<AttendanceRecordEntity> records = attendanceRecordRepository.findBySessionClassroomId(classroomId);

        // Group records by User ID
        Map<Long, List<AttendanceRecordEntity>> recordsByUser = records.stream()
                .collect(Collectors.groupingBy(r -> r.getUser().getId()));

        List<StudentStatDTO> studentStats = new ArrayList<>();

        for (ClassParticipantEntity participant : participants) {
            UserEntity user = participant.getUser();
            StudentStatDTO dto = new StudentStatDTO();
            dto.setUserId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFullName(user.getFullname());

            List<AttendanceRecordEntity> userRecords = recordsByUser.getOrDefault(user.getId(),
                    Collections.emptyList());

            int present = (int) userRecords.stream().filter(r -> "PRESENT".equals(r.getStatus())).count();
            int late = (int) userRecords.stream().filter(r -> "LATE".equals(r.getStatus())).count();

            dto.setPresentCount(present);
            dto.setLateCount(late);

            int attended = present + late;
            int absent = Math.max(0, totalSessions - attended);
            dto.setAbsentCount(absent);

            dto.setTotalSessions(totalSessions);

            if (totalSessions > 0) {
                double rate = (double) attended / totalSessions * 100;
                dto.setAttendanceRate(Math.round(rate * 100.0) / 100.0);
            } else {
                dto.setAttendanceRate(100.0);
            }

            studentStats.add(dto);
        }
        stats.setStudentStats(studentStats);

        Map<String, Integer> overall = new HashMap<>();
        overall.put("PRESENT",
                studentStats.stream().mapToInt(StudentStatDTO::getPresentCount).sum());
        overall.put("LATE",
                studentStats.stream().mapToInt(StudentStatDTO::getLateCount).sum());
        overall.put("ABSENT",
                studentStats.stream().mapToInt(StudentStatDTO::getAbsentCount).sum());

        stats.setOverallDidAttend(overall);

        return stats;
    }

    @Override
    public SessionStatsDTO getSessionStatistics(Long sessionId) throws Exception {
        SessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new Exception("Phiên điểm danh không tồn tại."));

        SessionStatsDTO dto = new SessionStatsDTO();
        dto.setSessionId(sessionId);
        dto.setSessionName(session.getName());

        // Total Students = Participants in Class
        int totalStudents = session.getClassroom().getParticipants().size();
        dto.setTotalStudents(totalStudents);

        int present = (int) attendanceRecordRepository.countBySessionIdAndStatus(sessionId, "PRESENT");
        int late = (int) attendanceRecordRepository.countBySessionIdAndStatus(sessionId, "LATE");

        dto.setPresentCount(present);
        dto.setLateCount(late);

        // Absent = Total - (Present + Late)
        int absent = totalStudents - (present + late);
        dto.setAbsentCount(Math.max(0, absent));

        return dto;
    }

    @Override
    public byte[] exportClassroomStatsToExcel(Long classroomId) throws Exception {
        ClassroomStatsDTO stats = getClassroomStatistics(classroomId);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(
                100)) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Thống kê điểm danh");

            // --- 1. KHỞI TẠO STYLE DÙNG CHUNG ---
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // --- 2. XỬ LÝ HEADER ---
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = { "STT", "Mã SV", "Họ tên", "Tổng buổi", "Có mặt", "Đi muộn", "Vắng", "Tỷ lệ (%)" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- 3. XỬ LÝ DATA ---
            int rowNum = 1;
            for (StudentStatDTO s : stats.getStudentStats()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum - 1);
                row.createCell(1).setCellValue(s.getUsername());
                row.createCell(2).setCellValue(s.getFullName());
                row.createCell(3).setCellValue(s.getTotalSessions());
                row.createCell(4).setCellValue(s.getPresentCount());
                row.createCell(5).setCellValue(s.getLateCount());
                row.createCell(6).setCellValue(s.getAbsentCount());
                row.createCell(7).setCellValue(s.getAttendanceRate());
            }

            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            workbook.write(out);
            workbook.dispose();
            return out.toByteArray();
        }
    }
}
