package com.morkath.scan2class.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.morkath.scan2class")
public class AppConfig {
	@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
