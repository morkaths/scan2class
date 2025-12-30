package com.morkath.scan2class.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.constant.auth.RoleCode;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.AuthService;
import com.morkath.scan2class.util.PasswordUtil;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public boolean register(String username, String password, String email) {
		if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
			return false;
		}
		UserEntity user = new UserEntity(username, PasswordUtil.hash(password), email);
		user.addRole(roleRepository.findByCode(RoleCode.USER.name()));
		return userRepository.save(user) != null;
	}
	
}
