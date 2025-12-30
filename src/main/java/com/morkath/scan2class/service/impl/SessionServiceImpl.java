package com.morkath.scan2class.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.repository.attendance.SessionRepository;
import com.morkath.scan2class.service.SessionService;

@Service
public class SessionServiceImpl extends BaseServiceImpl<SessionEntity, Long> implements SessionService {

    private SessionRepository sessionRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository) {
        super(sessionRepository);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionEntity getByToken(String token) {
        return sessionRepository.findByToken(token);
    }

    @Override
    public void closeSession(Long sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId).orElse(null);
        if (session != null) {
            session.setActive(false);
            session.setEndTime(java.time.LocalDateTime.now());
            sessionRepository.save(session);
        }
    }
}
