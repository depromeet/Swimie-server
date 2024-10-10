package com.depromeet.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
@Profile("prod")
public class DataSourceConfig {
    private static final String PRIMARY_SOURCE = "primaryDataSource";
    private static final String SECONDARY_SOURCE = "secondaryDataSource";

    @Bean(PRIMARY_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.primary.hikari")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(SECONDARY_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.secondary.hikari")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier(PRIMARY_SOURCE) DataSource primaryDataSource,
            @Qualifier(SECONDARY_SOURCE) DataSource secondaryDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("primary", primaryDataSource);
        dataSourceMap.put("secondary", secondaryDataSource);

        Map<Object, Object> immutableDataSourceMap = Collections.unmodifiableMap(dataSourceMap);

        routingDataSource.setTargetDataSources(immutableDataSourceMap);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
