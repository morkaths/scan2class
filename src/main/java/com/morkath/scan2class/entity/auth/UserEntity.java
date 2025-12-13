package com.morkath.scan2class.entity.auth;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.morkath.scan2class.core.BaseEntity;

@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = @Column(name = "uid"))
public class UserEntity extends BaseEntity {

	@Column(name = "username", nullable = false, unique = true, length = 100)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "email", nullable = false, unique = true, length = 150)
	private String email;

	@Column(name = "fullname", nullable = true, length = 150)
	private String fullname;

	@Column(name = "status", nullable = false)
	private int status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "uid"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<>();

	public UserEntity() {
		super();
	}
	
	public UserEntity(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.status = 1;
	}

	public UserEntity(String username, String password, String email, String fullname, int status) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.fullname = fullname;
		this.status = status;
	}
	
//	Helper Methods
	
	public void addRole(RoleEntity role) {
		this.roles.add(role);
	}

	public void removeRole(RoleEntity role) {
		this.roles.remove(role);
	}
	
//	Setters and Getters

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
		UserEntity user = (UserEntity) o;
		return username != null && username.equals(user.username);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
