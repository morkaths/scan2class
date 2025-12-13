package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.dto.UserDto;

public interface UserService extends BaseService<UserDto, Long> {
	UserDto getByUsername(String username);
	UserDto getCurrent();
}
