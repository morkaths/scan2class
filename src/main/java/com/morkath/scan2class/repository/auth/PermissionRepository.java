package com.morkath.scan2class.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morkath.scan2class.entity.auth.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

	boolean existsByCode(String code);

	PermissionEntity findByCode(String code);

	PermissionEntity findByName(String name);

	@Query("""
			SELECT p FROM PermissionEntity p
			JOIN p.roles r
			WHERE r.id = :role_id
			""")
	List<PermissionEntity> findByRoleId(@Param("role_id") Long roleId);

}
