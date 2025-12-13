package com.morkath.scan2class.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.dto.PermissionDto;
import com.morkath.scan2class.entity.auth.PermissionEntity;
import com.morkath.scan2class.mapper.PermissionMapper;
import com.morkath.scan2class.repository.auth.PermissionRepository;
import com.morkath.scan2class.service.PermissionService;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, PermissionDto, Long> implements PermissionService {
	private final PermissionRepository permissionRepository;
	private final PermissionMapper permissionMapper;
	
	public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
		super(permissionRepository, permissionMapper);
		this.permissionRepository = permissionRepository;
		this.permissionMapper = permissionMapper;
	}

	@Override
	public PermissionDto findByCode(String code) {
		List<PermissionEntity> entities = permissionRepository.findAll();
		return entities.stream()
				.filter(entity -> entity.getCode().equals(code))
				.findFirst()
				.map(permissionMapper::toDto)
				.orElse(null);
	}
	
	
}
