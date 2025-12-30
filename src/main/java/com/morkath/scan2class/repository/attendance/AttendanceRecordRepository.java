package com.morkath.scan2class.repository.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.morkath.scan2class.entity.attendance.AttendanceRecordEntity;
import com.morkath.scan2class.entity.attendance.SessionEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordEntity, Long> {
    AttendanceRecordEntity findBySessionAndUser(SessionEntity session, UserEntity user);

    boolean existsBySessionIdAndUserId(Long sessionId, Long userId);

    @Modifying
    @Query("DELETE FROM AttendanceRecordEntity ar WHERE ar.user.id = :userId AND ar.session.id IN (SELECT s.id FROM SessionEntity s WHERE s.classroom.id = :classroomId)")
    void deleteByStudentAndClassroom(@Param("userId") Long userId, @Param("classroomId") Long classroomId);
}
