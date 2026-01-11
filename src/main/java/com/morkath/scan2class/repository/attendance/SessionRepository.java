package com.morkath.scan2class.repository.attendance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.morkath.scan2class.entity.attendance.SessionEntity;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    SessionEntity findByToken(String token);

    int countByClassroomId(Long classroomId);

    List<SessionEntity> findByClassroomIdOrderByStartTimeDesc(Long classroomId);

    List<SessionEntity> findByActiveTrueAndEndTimeBefore(LocalDateTime now);

    // Last session
    SessionEntity findFirstByClassroomIdAndStartTimeBeforeOrderByStartTimeDesc(Long classroomId, LocalDateTime now);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE SessionEntity s SET s.active = false WHERE s.active = true AND s.endTime < :now")
    int deactivateExpiredSessions(@org.springframework.data.repository.query.Param("now") LocalDateTime now);
}
