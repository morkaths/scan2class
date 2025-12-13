package com.morkath.scan2class.service;

import java.util.List;

import com.morkath.scan2class.entity.auth.PermissionEntity;

public interface PermissionService {
	
	List<PermissionEntity> getList();
	PermissionEntity getById(Long id);
	PermissionEntity save(PermissionEntity permission);
	void delete(Long id);
}
