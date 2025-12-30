package com.morkath.scan2class.repository.classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.morkath.scan2class.entity.classroom.ClassParticipantEntity;
import com.morkath.scan2class.entity.classroom.ClassParticipantId;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

@Repository
public interface ClassParticipantRepository extends JpaRepository<ClassParticipantEntity, ClassParticipantId> {

    List<ClassParticipantEntity> findByIdClassId(Long classId);

    List<ClassParticipantEntity> findByIdUserId(Long userId);

    List<ClassParticipantEntity> findByClassroom(ClassroomEntity classroom);
    List<ClassParticipantEntity> findByUser(UserEntity user);

    boolean existsByIdClassIdAndIdUserId(Long classId, Long userId);
    void deleteByIdClassIdAndIdUserId(Long classId, Long userId);
}
