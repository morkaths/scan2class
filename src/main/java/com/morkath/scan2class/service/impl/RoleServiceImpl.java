package com.morkath.scan2class.service.impl;

import org.springframework.stereotype.Service;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.auth.RoleEntity;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleEntity, Long> implements RoleService {
	private final RoleRepository roleRepository;
	
	public RoleServiceImpl(RoleRepository roleRepository) {
		super(roleRepository);
		this.roleRepository = roleRepository;
	}

	@Override
	public RoleEntity getByCode(String code) {
		return roleRepository.findByCode(code);
	}

}
