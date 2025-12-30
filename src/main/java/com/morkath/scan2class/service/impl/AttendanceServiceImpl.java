package com.morkath.scan2class.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.attendance.AttendanceRecordEntity;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.attendance.AttendanceRecordRepository;
import com.morkath.scan2class.repository.attendance.SessionRepository;
import com.morkath.scan2class.service.AttendanceService;

@Service
@Transactional
public class AttendanceServiceImpl extends BaseServiceImpl<AttendanceRecordEntity, Long>
        implements AttendanceService {

    private final SessionRepository sessionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @org.springframework.beans.factory.annotation.Value("${demo.mode.skip-location:false}")
    private boolean skipLocationCheck;

    @Autowired
    public AttendanceServiceImpl(AttendanceRecordRepository attendanceRecordRepository,
            SessionRepository sessionRepository) {
        super(attendanceRecordRepository);
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void attend(UserEntity user, String token, Double lat, Double lon, Double accuracy, String deviceInfo)
            throws Exception {
        // ... (Previous checks 1-5 remain same, assumed implied or I should verify I am
        // not overwriting them)
        // Wait, replace_file_content replaces contiguous blocks. I need to be careful.
        // I will use multi_replace for safer updates if I can't see the whole file.
        // But I have viewed the file. The method is lines 34-98.

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

            // Accuracy Margin Logic
            double dist = session.calculateDistance(lat, lon);
            double allowedRadius = session.getRadius();
            if (accuracy != null && accuracy > 0) {
                // Apply margin: allowed radius + student's accuracy (limit to acceptable value
                // if needed, but per requirements: radius + accuracy/2)
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
        record.setDeviceUid("BROWSER"); // Or session ID/cookie if needed

        // Determine Status based on time?
        // Logic: If checkin > startTime + gracePeriod -> LATE.
        // Since we don't have grace period config yet, let's assume PRESENT if within
        // session time.
        // Or maybe 15 mins rule? Let's default to PRESENT for now as per requirements.
        record.setStatus("PRESENT");

        save(record);
    }

}
