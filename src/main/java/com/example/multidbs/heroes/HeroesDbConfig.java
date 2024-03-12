package com.example.multidbs.heroes;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = "com.example.multidbs.heroes",
        entityManagerFactoryRef = "heroesEntityManagerFactory",
        transactionManagerRef = "heroesTransactionManager"
)
public class HeroesDbConfig {


    @Bean
    @ConfigurationProperties("dbs.heroes")
    public DataSourceProperties heroesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("dbs.heroes.hikari")
    public DataSource heroesDataSource(@Qualifier("heroesDataSourceProperties") DataSourceProperties properties) {
        return properties
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties("dbs.heroes.liquibase")
    public LiquibaseProperties heroesLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase heroesLiquibase(@Qualifier("heroesDataSource") DataSource dataSource) {
        LiquibaseProperties properties = heroesLiquibaseProperties();
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
    public LocalContainerEntityManagerFactoryBean heroesEntityManagerFactory(
            @Qualifier("heroesDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.multidbs.heroes")
                .persistenceUnit("heroes")
                .build();
    }

    @Bean
    public PlatformTransactionManager heroesTransactionManager(
            @Qualifier("heroesEntityManagerFactory") LocalContainerEntityManagerFactoryBean heroesEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(heroesEntityManagerFactory.getObject()));
    }
}
