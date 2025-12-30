package com.morkath.scan2class.repository.classroom;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.entity.auth.UserEntity;

@Repository
public interface ClassroomRepository extends JpaRepository<ClassroomEntity, Long> {
    Optional<ClassroomEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<ClassroomEntity> findByOwner(UserEntity owner);
    List<ClassroomEntity> findByOwnerId(Long ownerId);
    List<ClassroomEntity> findByStatus(int status);
}
