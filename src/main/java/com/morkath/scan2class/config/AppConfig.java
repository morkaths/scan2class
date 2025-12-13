package com.morkath.scan2class.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.morkath.scan2class")
public class AppConfig {
	// TODO: Add additional configuration if needed
}
