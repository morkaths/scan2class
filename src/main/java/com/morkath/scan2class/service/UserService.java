package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.auth.UserEntity;

public interface UserService extends BaseService<UserEntity, Long> {
	UserEntity getByUsername(String username);
	UserEntity getCurrent();
}
