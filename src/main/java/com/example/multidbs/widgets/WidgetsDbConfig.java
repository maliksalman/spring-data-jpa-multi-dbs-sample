package com.example.multidbs.widgets;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.multidbs.widgets",
        entityManagerFactoryRef = "widgetsEntityManagerFactory",
        transactionManagerRef = "widgetsTransactionManager"
)
public class WidgetsDbConfig {

    @Bean
    @ConfigurationProperties("dbs.widgets")
    public DataSourceProperties widgetsDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @ConfigurationProperties("dbs.widgets.hikari")
    public DataSource widgetsDataSource(@Qualifier("widgetsDataSourceProperties") DataSourceProperties properties) {
        return properties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties("dbs.widgets.liquibase")
    public LiquibaseProperties widgetsLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase widgetsLiquibase(@Qualifier("widgetsDataSource") DataSource dataSource) {
        LiquibaseProperties properties = widgetsLiquibaseProperties();
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabelFilter(properties.getLabelFilter());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean widgetsEntityManagerFactory(
            @Qualifier("widgetsDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.multidbs.widgets")
                .persistenceUnit("widgets")
                .build();
    }

    @Bean
    public PlatformTransactionManager widgetsTransactionManager(
            @Qualifier("widgetsEntityManagerFactory") LocalContainerEntityManagerFactoryBean widgetsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(widgetsEntityManagerFactory.getObject()));
    }
}
