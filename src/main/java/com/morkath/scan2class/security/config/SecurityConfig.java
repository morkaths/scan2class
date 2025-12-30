package com.morkath.scan2class.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.morkath.scan2class.constant.auth.RoleCode;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Bean
	@SuppressWarnings("deprecation")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(requests -> requests
            		.antMatchers("/", "/auth/**", "/assets/**", "/static/**", "/resources/**", "/css/**", "/js/**").permitAll()
            		.antMatchers("/profile/**", "/attend/**", "/classrooms/join/**").authenticated()
                    .antMatchers("/admin/**").hasAnyRole(RoleCode.ADMIN.name(), RoleCode.MANAGER.name(), RoleCode.STAFF.name())
                    .anyRequest().authenticated())
            .formLogin(login -> login
                    .loginPage("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)
                    .permitAll())
            .logout(logout -> logout
            	    .logoutUrl("/auth/logout")
            	    .logoutSuccessUrl("/auth/login?logout")
            	    .invalidateHttpSession(true)
            	    .deleteCookies("JSESSIONID")
            	    .clearAuthentication(true)
            	    .permitAll())
            .headers(headers -> headers
            	    .cacheControl(cache -> {}))
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
