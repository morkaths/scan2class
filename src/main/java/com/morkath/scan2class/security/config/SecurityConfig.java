package com.morkath.scan2class.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	@SuppressWarnings("deprecation")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(requests -> requests
            		.antMatchers("/profile/**").authenticated()
                    .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                    .anyRequest().permitAll())
            .formLogin(login -> login
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)
                    .permitAll())
            .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .permitAll())
            .sessionManagement(management -> management
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false))
            .exceptionHandling(exception -> exception
                    .accessDeniedPage("/error/403"));

        return http.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
