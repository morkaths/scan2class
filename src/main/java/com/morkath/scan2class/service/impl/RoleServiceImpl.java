package com.morkath.scan2class.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.entity.auth.RoleEntity;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RoleEntity> getList() {
		return roleRepository.findAll();
	}

	@Override
	public RoleEntity getById(Long id) {
		return roleRepository.findById(id).orElse(null);
	}

	@Override
	public RoleEntity save(RoleEntity role) {
		return roleRepository.save(role);
	}

	@Override
	public void delete(Long id) {
		roleRepository.deleteById(id);		
	}

}
