package com.morkath.scan2class.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.entity.auth.PermissionEntity;
import com.morkath.scan2class.repository.auth.PermissionRepository;
import com.morkath.scan2class.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
    private PermissionRepository permissionRepository;
	
	@Override
	public List<PermissionEntity> getList() {
		return permissionRepository.findAll();
	}

	@Override
	public PermissionEntity getById(Long id) {
		return permissionRepository.findById(id).orElse(null);
	}

	@Override
	public PermissionEntity save(PermissionEntity permission) {
		return permissionRepository.save(permission);
	}

	@Override
	public void delete(Long id) {
		permissionRepository.deleteById(id);		
	}
	
	

}
