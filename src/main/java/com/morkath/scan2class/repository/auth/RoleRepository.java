package com.morkath.scan2class.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import com.morkath.scan2class.entity.auth.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
	boolean existsByCode(String code);

	RoleEntity findByCode(String code);

	RoleEntity findByName(String name);
}