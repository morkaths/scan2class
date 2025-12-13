package com.morkath.scan2class.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	@Override
    protected Class<?>[] getRootConfigClasses() {
    	return new Class<?>[] { 
	        AppConfig.class, 
	        DataSourceConfig.class,
	        JpaConfig.class,
	        JpaAuditingConfig.class
	    };
    }
	
	@Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }
	
	@Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
	
}
