package com.morkath.scan2class.service.impl;

import org.springframework.stereotype.Service;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.auth.PermissionEntity;
import com.morkath.scan2class.repository.auth.PermissionRepository;
import com.morkath.scan2class.service.PermissionService;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, Long> implements PermissionService {
	private final PermissionRepository permissionRepository;
	
	public PermissionServiceImpl(PermissionRepository permissionRepository) {
		super(permissionRepository);
		this.permissionRepository = permissionRepository;
	}

	@Override
	public PermissionEntity getByCode(String code) {
		return permissionRepository.findByCode(code);
	}
	
	
}
