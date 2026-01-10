package com.morkath.scan2class.task;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.morkath.scan2class.repository.attendance.SessionRepository;

@Component
public class SessionCleanupTask {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SessionCleanupTask.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupExpiredSessions() {
        try {
            LocalDateTime now = LocalDateTime.now();
            int updatedCount = sessionRepository.deactivateExpiredSessions(now);

            if (updatedCount > 0) {
                logger.info("Session Cleanup: Closed {} expired sessions at {}", updatedCount, now);
            }
        } catch (Exception e) {
            logger.error("Error occurred while cleaning up expired sessions", e);
        }
    }
}
