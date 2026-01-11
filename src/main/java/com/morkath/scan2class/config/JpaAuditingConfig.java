package com.morkath.scan2class.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}

	public static class AuditorAwareImpl implements AuditorAware<String> {
		@Override
		public Optional<String> getCurrentAuditor() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null || !authentication.isAuthenticated()
					|| authentication.getPrincipal().equals("anonymousUser")) {
				return Optional.of("system");
			}

			Object principal = authentication.getPrincipal();

			// Case 1: Form Login (AuthUser)
			if (principal instanceof com.morkath.scan2class.security.model.AuthUser) {
				return Optional
						.ofNullable(((com.morkath.scan2class.security.model.AuthUser) principal).getUser().getEmail());
			}

			// Case 2: OAuth2 / OIDC Login (Google)
			if (principal instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser) {
				return Optional.ofNullable(
						((org.springframework.security.oauth2.core.oidc.user.OidcUser) principal).getEmail());
			}

			// Case 3: OAuth2User (Generic)
			if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
				String email = ((org.springframework.security.oauth2.core.user.OAuth2User) principal)
						.getAttribute("email");
				if (email != null) {
					return Optional.of(email);
				}
			}

			// Fallback to username
			return Optional.ofNullable(authentication.getName());
		}
	}
}
