package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.dto.ClassroomStatsDTO;
import com.morkath.scan2class.dto.SessionStatsDTO;
import com.morkath.scan2class.dto.StudentStatDTO;
import com.morkath.scan2class.entity.attendance.AttendanceRecordEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

public interface AttendanceService extends BaseService<AttendanceRecordEntity, Long> {

        void attend(UserEntity user, String token, Double lat, Double lon, Double accuracy, String deviceInfo)
                        throws Exception;

        void updateAttendanceStatus(Long userId, Long sessionId, String status, String note, UserEntity teacher)
                        throws Exception;

        ClassroomStatsDTO getClassroomStatistics(Long classroomId) throws Exception;

        SessionStatsDTO getSessionStatistics(Long sessionId) throws Exception;

        byte[] exportClassroomStatsToExcel(Long classroomId) throws Exception;

        StudentStatDTO getStudentClassStats(Long classroomId, Long userId);
}
