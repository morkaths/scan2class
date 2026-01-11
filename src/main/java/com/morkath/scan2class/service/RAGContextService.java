package com.morkath.scan2class.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morkath.scan2class.dto.ClassroomStatsDTO;
import com.morkath.scan2class.dto.StudentStatDTO;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.repository.classroom.ClassroomRepository;
import com.morkath.scan2class.repository.classroom.ClassParticipantRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.repository.attendance.SessionRepository;

@Service
@Transactional(readOnly = true)
public class RAGContextService {

    private final AttendanceService attendanceService;
    private final ClassroomRepository classroomRepository;
    private final ClassParticipantRepository classParticipantRepository;
    private final SessionRepository sessionRepository;
    private final ObjectMapper objectMapper;

    public RAGContextService(AttendanceService attendanceService,
            ClassroomRepository classroomRepository,
            ClassParticipantRepository classParticipantRepository,
            SessionRepository sessionRepository,
            ObjectMapper objectMapper) {
        this.attendanceService = attendanceService;
        this.classroomRepository = classroomRepository;
        this.classParticipantRepository = classParticipantRepository;
        this.sessionRepository = sessionRepository;
        this.objectMapper = objectMapper;
    }

    public String getContextForUser(UserEntity user) {
        try {
            Map<String, Object> fullContext = new HashMap<>();

            // 1. Get Teaching Data (Classes owned)
            List<ClassroomEntity> ownedClasses = classroomRepository.findByOwnerId(user.getId());
            if (!ownedClasses.isEmpty()) {
                fullContext.put("teaching", getTeacherContextData(user, ownedClasses));
            }

            // 2. Get Studying Data (Classes joined)
            List<ClassroomEntity> joinedClasses = classParticipantRepository.findByUser(user)
                    .stream().map(p -> p.getClassroom()).collect(Collectors.toList());
            if (!joinedClasses.isEmpty()) {
                fullContext.put("learning", getStudentContextData(user, joinedClasses));
            }

            return objectMapper.writeValueAsString(fullContext);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    private List<Object> getStudentContextData(UserEntity student, List<ClassroomEntity> myClasses) throws Exception {
        List<Object> classData = new ArrayList<>();

        for (ClassroomEntity classroom : myClasses) {
            // Optimized call: Get only specific student stats
            StudentStatDTO myStat = attendanceService.getStudentClassStats(classroom.getId(), student.getId());

            if (myStat != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("subject", classroom.getName());
                data.put("room", classroom.getRoom());
                data.put("total_sessions_so_far", myStat.getTotalSessions());
                data.put("attended", myStat.getPresentCount() + myStat.getLateCount());
                data.put("absent", myStat.getAbsentCount());
                data.put("late", myStat.getLateCount());
                data.put("attendance_rate", myStat.getAttendanceRate());
                data.put("policy", "3 LATE = 1 ABSENT. Max 20% ABSENT allowed.");

                // Get last session
                LocalDateTime now = LocalDateTime.now();
                SessionEntity lastSession = sessionRepository
                        .findFirstByClassroomIdAndStartTimeBeforeOrderByStartTimeDesc(classroom.getId(), now);
                if (lastSession != null) {
                    data.put("last_session_time",
                            lastSession.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));

                    // Prioritize specific session room if available
                    if (lastSession.getRoom() != null && !lastSession.getRoom().isEmpty()) {
                        data.put("room", lastSession.getRoom() + " (Phòng thay đổi)");
                    }

                    boolean attended = lastSession.getRecords().stream()
                            .anyMatch(r -> r.getUser().getId().equals(student.getId()));
                    data.put("last_session_status", attended ? "Đã điểm danh" : "Vắng mặt");
                } else {
                    data.put("last_session_time", "Chưa có");
                    data.put("last_session_status", "N/A");
                }

                classData.add(data);
            }
        }

        return classData;
    }

    private List<Map<String, Object>> getTeacherContextData(UserEntity teacher, List<ClassroomEntity> ownedClasses)
            throws Exception {
        List<Map<String, Object>> classes = new ArrayList<>();

        for (ClassroomEntity classroom : ownedClasses) {
            ClassroomStatsDTO stats = attendanceService.getClassroomStatistics(classroom.getId());

            // Identify at-risk students (e.g., absent > 20% or > 2 sessions)
            List<String> atRiskStudents = stats.getStudentStats().stream()
                    .filter(s -> s.getAbsentCount() >= 3) // Threshold example
                    .map(s -> s.getFullName() + " (Vắng " + s.getAbsentCount() + ")")
                    .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("className", classroom.getName());
            data.put("room", classroom.getRoom());
            data.put("total_students", stats.getStudentStats().size());
            data.put("total_sessions", stats.getTotalSessions());
            data.put("warning_students", atRiskStudents);

            classes.add(data);
        }

        return classes;
    }
}
