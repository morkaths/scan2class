package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.entity.auth.PermissionEntity;

public interface PermissionService extends BaseService<PermissionEntity, Long> {
	PermissionEntity getByCode(String code);
}
