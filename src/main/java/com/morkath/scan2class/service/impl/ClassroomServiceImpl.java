package com.morkath.scan2class.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.entity.classroom.ClassroomEntity;
import com.morkath.scan2class.repository.attendance.AttendanceRecordRepository;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.repository.classroom.ClassroomRepository;
import com.morkath.scan2class.service.ClassroomService;

@Service
public class ClassroomServiceImpl extends BaseServiceImpl<ClassroomEntity, Long> implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final UserRepository userRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository,
            AttendanceRecordRepository attendanceRecordRepository,
            UserRepository userRepository) {
        super(classroomRepository);
        this.classroomRepository = classroomRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ClassroomEntity getByCode(String code) {
        return classroomRepository.findByCode(code).orElse(null);
    }

    @Override
    public java.util.List<ClassroomEntity> getByOwner(UserEntity owner) {
        return classroomRepository.findByOwner(owner);
    }

    @Override
    @Transactional
    public void removeStudentFromClass(Long userId, Long classroomId) {
        ClassroomEntity classroom = getById(classroomId);
        UserEntity user = userRepository.findById(userId).orElse(null);

        if (classroom != null && user != null) {
            // Remove from participants
            classroom.removeParticipant(user);
            save(classroom);

            // Remove attendance
            attendanceRecordRepository.deleteByStudentAndClassroom(userId, classroomId);
        }
    }
}
