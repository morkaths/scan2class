package com.morkath.scan2class.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class AppProperties {

    // Database
    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    // HikariCP
    @Value("${hikari.maximumPoolSize:10}")
    private Integer hikariMaximumPoolSize;

    @Value("${hikari.minimumIdle:2}")
    private Integer hikariMinimumIdle;

    @Value("${hikari.idleTimeout:30000}")
    private Long hikariIdleTimeout;

    @Value("${hikari.connectionTimeout:30000}")
    private Long hikariConnectionTimeout;

    @Value("${hikari.poolName:HikariCP}")
    private String hikariPoolName;

    @Value("${hikari.maxLifetime:1800000}")
    private Long hikariMaxLifetime;

    // Cache SQL & DataSource Props
    @Value("${hikari.dataSource.cachePrepStmts:true}")
    private Boolean dataSourceCachePrepStmts;

    @Value("${hikari.dataSource.prepStmtCacheSize:250}")
    private Integer dataSourcePrepStmtCacheSize;

    @Value("${hikari.dataSource.prepStmtCacheSqlLimit:2048}")
    private Integer dataSourcePrepStmtCacheSqlLimit;

    @Value("${hikari.dataSource.useServerPrepStmts:true}")
    private Boolean dataSourceUseServerPrepStmts;

    @Value("${hikari.dataSource.useLocalSessionState:true}")
    private Boolean dataSourceUseLocalSessionState;

    @Value("${hikari.dataSource.rewriteBatchedStatements:true}")
    private Boolean dataSourceRewriteBatchedStatements;

    @Value("${hikari.dataSource.cacheResultSetMetadata:true}")
    private Boolean dataSourceCacheResultSetMetadata;

    @Value("${hikari.dataSource.cacheServerConfiguration:true}")
    private Boolean dataSourceCacheServerConfiguration;

    @Value("${hikari.dataSource.elideSetAutoCommits:true}")
    private Boolean dataSourceElideSetAutoCommits;

    @Value("${hikari.dataSource.maintainTimeStats:false}")
    private Boolean dataSourceMaintainTimeStats;

    // Hibernate
    @Value("${hibernate.dialect:org.hibernate.dialect.MySQL8Dialect}")
    private String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto:update}")
    private String hibernateHbm2ddlAuto;

    @Value("${hibernate.show_sql:true}")
    private String hibernateShowSql;

    @Value("${hibernate.format_sql:true}")
    private String hibernateFormatSql;

    @Value("${hibernate.enable_lazy_load_no_trans:true}")
    private String hibernateEnableLazyLoadNoTrans;

    // Google OAuth2
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Integer getHikariMaximumPoolSize() {
        return hikariMaximumPoolSize;
    }

    public Integer getHikariMinimumIdle() {
        return hikariMinimumIdle;
    }

    public Long getHikariIdleTimeout() {
        return hikariIdleTimeout;
    }

    public Long getHikariConnectionTimeout() {
        return hikariConnectionTimeout;
    }

    public Long getHikariMaxLifetime() {
        return hikariMaxLifetime;
    }

    public String getHikariPoolName() {
        return hikariPoolName;
    }

    public Boolean getDataSourceCachePrepStmts() {
        return dataSourceCachePrepStmts;
    }

    public Integer getDataSourcePrepStmtCacheSize() {
        return dataSourcePrepStmtCacheSize;
    }

    public Integer getDataSourcePrepStmtCacheSqlLimit() {
        return dataSourcePrepStmtCacheSqlLimit;
    }

    public Boolean getDataSourceUseServerPrepStmts() {
        return dataSourceUseServerPrepStmts;
    }

    public Boolean getDataSourceUseLocalSessionState() {
        return dataSourceUseLocalSessionState;
    }

    public Boolean getDataSourceRewriteBatchedStatements() {
        return dataSourceRewriteBatchedStatements;
    }

    public Boolean getDataSourceCacheResultSetMetadata() {
        return dataSourceCacheResultSetMetadata;
    }

    public Boolean getDataSourceCacheServerConfiguration() {
        return dataSourceCacheServerConfiguration;
    }

    public Boolean getDataSourceElideSetAutoCommits() {
        return dataSourceElideSetAutoCommits;
    }

    public Boolean getDataSourceMaintainTimeStats() {
        return dataSourceMaintainTimeStats;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHibernateHbm2ddlAuto() {
        return hibernateHbm2ddlAuto;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }

    public String getHibernateFormatSql() {
        return hibernateFormatSql;
    }

    public String getHibernateEnableLazyLoadNoTrans() {
        return hibernateEnableLazyLoadNoTrans;
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getGoogleClientSecret() {
        return googleClientSecret;
    }
}
