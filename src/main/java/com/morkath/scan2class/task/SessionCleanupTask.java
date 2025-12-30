package com.morkath.scan2class.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.repository.attendance.SessionRepository;

@Component
public class SessionCleanupTask {

    @Autowired
    private SessionRepository sessionRepository;

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<SessionEntity> expiredSessions = sessionRepository.findByActiveTrueAndEndTimeBefore(now);

        if (!expiredSessions.isEmpty()) {
            for (SessionEntity session : expiredSessions) {
                session.setActive(false);
            }
            sessionRepository.saveAll(expiredSessions);
            System.out.println("Closed " + expiredSessions.size() + " expired sessions.");
        }
    }
}
