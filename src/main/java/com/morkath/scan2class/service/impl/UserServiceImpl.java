package com.morkath.scan2class.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.constant.auth.RoleCode;
import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.UserService;
import com.morkath.scan2class.util.PasswordUtil;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		super(userRepository);
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserEntity getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserEntity getCurrent() {
		return userRepository.findByUsername(
				SecurityContextHolder.getContext().getAuthentication().getName()
		);
	}
	
	@Override
	public UserEntity save(UserEntity entity) {
		if (entity.getPassword() != null) {
			entity.setPassword(PasswordUtil.hash(entity.getPassword()));
		}
		if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
			entity.addRole(roleRepository.findByCode(RoleCode.USER.name()));
		}
		return super.save(entity);
	}
	
}
