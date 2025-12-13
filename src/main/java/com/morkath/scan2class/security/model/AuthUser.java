package com.morkath.scan2class.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.morkath.scan2class.entity.auth.UserEntity;

public class AuthUser extends User {
	private static final long serialVersionUID = 1L;
	private UserEntity user;
	
	public AuthUser(UserEntity user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), user.getStatus() == 1, true, true, true, authorities);
		this.user = user;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
