package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.attendance.AttendanceRecordEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

public interface AttendanceService extends BaseService<AttendanceRecordEntity, Long> {

    void attend(UserEntity user, String token, Double lat, Double lon, Double accuracy, String deviceInfo)
            throws Exception;

}
