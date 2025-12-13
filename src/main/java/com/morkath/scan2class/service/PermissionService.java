package com.morkath.scan2class.service;

import com.morkath.scan2class.core.BaseService;
import com.morkath.scan2class.dto.PermissionDto;

public interface PermissionService extends BaseService<PermissionDto, Long> {
	PermissionDto findByCode(String code);
}
