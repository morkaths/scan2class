package com.morkath.scan2class.service.impl;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.morkath.scan2class.constant.auth.RoleCode;
import com.morkath.scan2class.core.BaseServiceImpl;
import com.morkath.scan2class.entity.auth.UserEntity;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.UserService;
import com.morkath.scan2class.util.PasswordUtil;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity, Long> implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		super(userRepository);
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public UserEntity getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public UserEntity getCurrent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.err.println("DEBUG: UserServiceImpl.getCurrent() called");

		if (authentication == null) {
			System.err.println("DEBUG: Authentication is NULL");
			return null;
		}

		System.err.println("DEBUG: Auth class: " + authentication.getClass().getName());
		System.err.println("DEBUG: Is Authenticated: " + authentication.isAuthenticated());
		System.err.println("DEBUG: Original Name: " + authentication.getName());

		if (!authentication.isAuthenticated()) {
			return null;
		}

		String username = authentication.getName();

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			System.err.println("DEBUG: Principal class: " + oauthToken.getPrincipal().getClass().getName());

			if (oauthToken.getPrincipal() instanceof OidcUser) {
				OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();
				System.err.println("DEBUG: OidcUser Email: " + oidcUser.getEmail());
				// Google Login uses Email as username in our system
				if (oidcUser.getEmail() != null) {
					username = oidcUser.getEmail();
				}
			} else if (oauthToken.getPrincipal() instanceof OAuth2User) {
				OAuth2User oauth2User = oauthToken.getPrincipal();
				String email = oauth2User.getAttribute("email");
				System.err.println("DEBUG: OAuth2User Email: " + email);
				if (email != null) {
					username = email;
				}
			}
		}

		System.err.println("DEBUG: Lookup Username: " + username);
		UserEntity user = userRepository.findByUsername(username);
		System.err.println("DEBUG: Found User: " + (user != null ? user.getId() : "NULL"));

		return user;
	}

	@Override
	@Transactional
	public UserEntity save(UserEntity entity) {
		if (entity.getPassword() != null) {
			// Check if password is likely already hashed (BCrypt is 60 chars, starts with
			// $2)
			boolean isAlreadyHashed = entity.getPassword().length() == 60 && entity.getPassword().startsWith("$2");
			if (!isAlreadyHashed) {
				entity.setPassword(PasswordUtil.hash(entity.getPassword()));
			}
		}
		if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
			entity.addRole(roleRepository.findByCode(RoleCode.USER.name()));
		}
		return super.save(entity);
	}

}
