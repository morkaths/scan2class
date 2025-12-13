package com.morkath.scan2class.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class DataSourceConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("db.driver"));
        config.setJdbcUrl(env.getProperty("db.url"));
        config.setUsername(env.getProperty("db.user"));
        config.setPassword(env.getProperty("db.password"));

        config.setMaximumPoolSize(Integer.parseInt(env.getProperty("hikari.maximumPoolSize", "10")));
        config.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.minimumIdle", "2")));
        config.setIdleTimeout(Long.parseLong(env.getProperty("hikari.idleTimeout", "30000")));
        config.setConnectionTimeout(Long.parseLong(env.getProperty("hikari.connectionTimeout", "30000")));
        config.setPoolName(env.getProperty("hikari.poolName", "HikariCP"));

        return new HikariDataSource(config);
    }
}
