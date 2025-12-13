package com.morkath.scan2class.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.dto.UserDto;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.mapper.UserMapper;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, UserDto, Long> implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		super(userRepository, userMapper);
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public UserDto getByUsername(String username) {
		UserEntity userEntity = userRepository.findByUsername(username);
		return userMapper.toDto(userEntity);
	}
	
	@Override
	public UserDto getCurrent() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return this.getByUsername(username);
	}
}
