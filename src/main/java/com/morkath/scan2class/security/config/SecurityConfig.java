package com.morkath.scan2class.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

import com.morkath.scan2class.constant.auth.RoleCode;
import com.morkath.scan2class.repository.auth.RoleRepository;
import com.morkath.scan2class.repository.auth.UserRepository;
import com.morkath.scan2class.service.AuthService;
import com.morkath.scan2class.security.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthService authService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(requests -> requests
						.antMatchers("/", "/error/**", "/auth/**", "/assets/**", "/static/**", "/resources/**",
								"/css/**", "/js/**")
						.permitAll()
						.antMatchers("/profile/**", "/attend/**", "/classrooms/join/**").authenticated()
						.antMatchers("/admin/**")
						.hasAnyRole(RoleCode.ADMIN.name(), RoleCode.MANAGER.name(), RoleCode.STAFF.name())
						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/auth/login")
						.defaultSuccessUrl("/", true)
						.permitAll())
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/auth/login")
						.defaultSuccessUrl("/", true)
						.userInfoEndpoint(userInfo -> userInfo
								.oidcUserService(new CustomOAuth2UserService(userRepository, authService))))
				.logout(logout -> logout
						.logoutUrl("/auth/logout")
						.logoutSuccessUrl("/auth/login?logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.clearAuthentication(true)
						.permitAll())
				.exceptionHandling(exception -> exception
						.accessDeniedPage("/error/403"))
				.securityContext(context -> context
						.requireExplicitSave(false))
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public static ClientRegistrationRepository clientRegistrationRepository(Environment env) {
		return new InMemoryClientRegistrationRepository(googleClientRegistration(env));
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(
			ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	private static ClientRegistration googleClientRegistration(Environment env) {
		String clientId = env.getProperty("google.client.id");
		String clientSecret = env.getProperty("google.client.secret");

		if (clientId == null || clientSecret == null) {
			throw new IllegalStateException("Missing Google credentials in application.properties");
		}

		return ClientRegistration.withRegistrationId("google")
				.clientId(clientId)
				.clientSecret(clientSecret)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
				.scope("openid", "profile", "email")
				.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
				.tokenUri("https://www.googleapis.com/oauth2/v4/token")
				.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
				.userNameAttributeName(IdTokenClaimNames.SUB)
				.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
				.clientName("Google")
				.build();
	}
}
