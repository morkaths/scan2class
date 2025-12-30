package com.morkath.scan2class.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {
private Long uid;
	
	@NotBlank(message = "Username is required")
	private String username;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Email is required")
	@Email(message = "Email should be valid")
	private String email;
	
	@NotBlank(message = "Fullname is required")
	private String fullname;
	
	private int status = 1;

	private Set<UserRoleDto> roles;
	
	private List<Long> roleIds;

	public UserDto() {
		super();
	}

	public UserDto(Long id, String username, String password, String email, String fullname, int status) {
		super();
		this.uid = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.status = status;
	}

	public UserDto(Long id, String username, String password, String email, String fullname, int status, Set<UserRoleDto> roles) {
		super();
		this.uid = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.status = status;
		this.roles = roles;
	}

	public Long getId() {
		return uid;
	}

	public void setId(Long id) {
		this.uid = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set<UserRoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRoleDto> roles) {
		this.roles = roles;
	}
	
	public List<Long> getRoleIds() {
		return roleIds;
	}
	
	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
}
