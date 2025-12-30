package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;

import com.morkath.scan2class.entity.auth.UserEntity;

public interface ClassroomService extends BaseService<ClassroomEntity, Long> {
    ClassroomEntity getByCode(String code);

    java.util.List<ClassroomEntity> getByOwner(UserEntity owner);

    void removeStudentFromClass(Long userId, Long classroomId);
}
