package com.morkath.scan2class.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserEntity> getList() {
		return userRepository.findAll();
	}

	@Override
	public UserEntity getCurrent() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return this.getByUsername(username);
	}

	@Override
	public UserEntity getById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public UserEntity getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserEntity save(UserEntity user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

}
