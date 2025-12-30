package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.auth.RoleEntity;

public interface RoleService extends BaseService<RoleEntity, Long> {
	RoleEntity getByCode(String code);
}
