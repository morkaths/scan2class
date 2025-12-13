package com.morkath.scan2class.service;

import java.util.List;

import com.morkath.scan2class.entity.auth.UserEntity;

public interface UserService {
	List<UserEntity> getList();
	UserEntity getCurrent();
	UserEntity getById(Long id);
	UserEntity getByUsername(String username);
	UserEntity save(UserEntity user);
	void delete(Long id);
}
