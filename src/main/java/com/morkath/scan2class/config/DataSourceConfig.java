package com.morkath.scan2class.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private AppProperties appProperties;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(appProperties.getDbDriver());
        config.setJdbcUrl(appProperties.getDbUrl());
        config.setUsername(appProperties.getDbUser());
        config.setPassword(appProperties.getDbPassword());

        config.setMaximumPoolSize(appProperties.getHikariMaximumPoolSize());
        config.setMinimumIdle(appProperties.getHikariMinimumIdle());
        config.setIdleTimeout(appProperties.getHikariIdleTimeout());
        config.setConnectionTimeout(appProperties.getHikariConnectionTimeout());
        config.setPoolName(appProperties.getHikariPoolName());
        config.setMaxLifetime(appProperties.getHikariMaxLifetime());

        // Cache SQL & DataSource Props
        config.addDataSourceProperty("cachePrepStmts", appProperties.getDataSourceCachePrepStmts());
        config.addDataSourceProperty("prepStmtCacheSize", appProperties.getDataSourcePrepStmtCacheSize());
        config.addDataSourceProperty("prepStmtCacheSqlLimit", appProperties.getDataSourcePrepStmtCacheSqlLimit());
        config.addDataSourceProperty("useServerPrepStmts", appProperties.getDataSourceUseServerPrepStmts());
        config.addDataSourceProperty("useLocalSessionState", appProperties.getDataSourceUseLocalSessionState());
        config.addDataSourceProperty("rewriteBatchedStatements", appProperties.getDataSourceRewriteBatchedStatements());
        config.addDataSourceProperty("cacheResultSetMetadata", appProperties.getDataSourceCacheResultSetMetadata());
        config.addDataSourceProperty("cacheServerConfiguration", appProperties.getDataSourceCacheServerConfiguration());
        config.addDataSourceProperty("elideSetAutoCommits", appProperties.getDataSourceElideSetAutoCommits());
        config.addDataSourceProperty("maintainTimeStats", appProperties.getDataSourceMaintainTimeStats());

        return new HikariDataSource(config);
    }
}
