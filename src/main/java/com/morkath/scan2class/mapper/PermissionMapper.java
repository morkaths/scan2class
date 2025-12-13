package com.morkath.scan2class.mapper;

import org.springframework.stereotype.Component;

import com.morkath.scan2class.core.BaseMapper;
import com.morkath.scan2class.dto.PermissionDto;
import com.morkath.scan2class.entity.auth.PermissionEntity;

@Component
public class PermissionMapper extends BaseMapper<PermissionEntity, PermissionDto> {

	@Override
	protected PermissionEntity createEntityInstance() {
		return new PermissionEntity();
	}

	@Override
	protected PermissionDto createDtoInstance() {
		return new PermissionDto();
	}
	
}
