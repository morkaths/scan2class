package com.morkath.scan2class.mapper;

import org.springframework.stereotype.Component;

import com.morkath.scan2class.core.BaseMapper;
import com.morkath.scan2class.dto.UserDto;
import com.morkath.scan2class.entity.auth.UserEntity;

@Component
public class UserMapper extends BaseMapper<UserEntity, UserDto> {

	@Override
	protected UserEntity createEntityInstance() {
		return new UserEntity();
	}

	@Override
	protected UserDto createDtoInstance() {
		return new UserDto();
	}
	
}
