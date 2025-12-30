package com.morkath.scan2class.mapper;

import org.springframework.stereotype.Component;

import com.morkath.scan2class.core.BaseMapper;
import com.morkath.scan2class.dto.RoleDto;
import com.morkath.scan2class.entity.auth.RoleEntity;

@Component
public class RoleMapper extends BaseMapper<RoleEntity, RoleDto> {

	@Override
	protected RoleEntity createEntityInstance() {
		return new RoleEntity();
	}

	@Override
	protected RoleDto createDtoInstance() {
		return new RoleDto();
	}

}
