package com.morkath.scan2class.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morkath.scan2class.entity.auth.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
	PermissionEntity findByCode(String code);
}
