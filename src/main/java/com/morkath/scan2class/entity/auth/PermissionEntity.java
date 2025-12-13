package com.morkath.scan2class.entity.auth;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.morkath.scan2class.core.BaseEntity;

@Entity
@Table(name = "permissions")
public class PermissionEntity extends BaseEntity {

	@Column(name = "code", nullable = false, unique = true, length = 100)
	private String code;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
	private Set<RoleEntity> roles = new HashSet<>();

	public PermissionEntity() {
		super();
	}

	public PermissionEntity(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
//	Setters and Getters

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PermissionEntity that = (PermissionEntity) o;
		return code != null && code.equals(that.code);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
