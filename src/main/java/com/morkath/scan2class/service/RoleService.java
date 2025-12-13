package com.morkath.scan2class.service;

import java.util.List;

import com.morkath.scan2class.entity.auth.RoleEntity;

public interface RoleService {
	List<RoleEntity> getList();
	RoleEntity getById(Long id);
	RoleEntity getByCode(String code);
	RoleEntity save(RoleEntity role);
	void delete(Long id);
}
