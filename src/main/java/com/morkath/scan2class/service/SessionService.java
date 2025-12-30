package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.attendance.SessionEntity;

public interface SessionService extends BaseService<SessionEntity, Long> {
    SessionEntity getByToken(String token);

    void closeSession(Long sessionId);
}
