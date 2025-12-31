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

    List<SessionEntity> findByActiveTrueAndEndTimeBefore(LocalDateTime now);
}
