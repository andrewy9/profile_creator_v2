package com.andrew.profile_creator.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@PropertySource({"application.yml"})
@EnableJpaRepositories(
        basePackages = "com.andrew.profile_creator.repository.accounts",
        entityManagerFactoryRef = "accountsEntityManager",
        transactionManagerRef = "accountsTransactionManager"
)
@Configuration
public class PersistenceAccountsAutoConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource.accounts")
    public DataSource accountsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate accountsJdbcTemplate(@Qualifier("accountsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean accountsEntityManager(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("accountsDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return  builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.andrew.profile_creator.models.accounts")
                .persistenceUnit("AppUser")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager accountsTransactionManager(
            @Qualifier("accountsEntityManager") LocalContainerEntityManagerFactoryBean accountsEntityManagerFactory
    ) {
        return new JpaTransactionManager(Objects.requireNonNull(accountsEntityManagerFactory.getObject()));
    }

}
