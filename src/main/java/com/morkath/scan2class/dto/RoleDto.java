package com.morkath.scan2class.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoleDto {
	private Long id;
	
	@NotBlank(message = "Code is required")
	@Size(max = 100, message = "Code must not exceed 100 characters")
    private String code;
	
	 @NotBlank(message = "Name is required")
	private String name;
	 
	private Set<PermissionDto> permissions = new HashSet<>();
	
	private List<Long> permissionIds = new ArrayList<>();

	public RoleDto() {
		super();
	}

	public RoleDto(Long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Set<PermissionDto> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionDto> permissions) {
		this.permissions = permissions;
	}
	
	public List<Long> getPermissionIds() {
		return permissionIds;
	}
	
	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
