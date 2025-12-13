package com.morkath.scan2class.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.entity.auth.RoleEntity;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.security.model.AuthUser;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Load user details by identifier.
	 * 
	 * @param username - the username or email of the user
	 * @return UserDetails object containing user information and authorities
	 * @throws UsernameNotFoundException if user is not found
	 */
	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsernameOrEmail(identifier);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with identifier: " + identifier);
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		for (RoleEntity role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
			role.getPermissions().forEach(permission -> {
				authorities.add(new SimpleGrantedAuthority(permission.getCode()));
			});
		}

		return new AuthUser(user, authorities);
	}

}
