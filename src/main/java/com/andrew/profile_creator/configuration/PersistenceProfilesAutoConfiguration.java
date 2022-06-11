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
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EnableJpaRepositories(
        basePackages = "com.andrew.profile_creator.repository.profiles",
        entityManagerFactoryRef = "profilesEntityManager",
        transactionManagerRef = "profilesTransactionManager"
)
@Configuration
public class PersistenceProfilesAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.profiles")
    public DataSource profilesDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate profilesJdbcTemplate(@Qualifier("profilesDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean profilesEntityManager(EntityManagerFactoryBuilder builder,
                                                                                   @Qualifier("profilesDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return  builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.andrew.profile_creator.models.profiles")
                .persistenceUnit("Profile")
                .build();
    }

    @Bean
    public PlatformTransactionManager profilesTransactionManager(
            @Qualifier("profilesEntityManager")LocalContainerEntityManagerFactoryBean profilesEntityManagerFactory
            ) {
        return new JpaTransactionManager(Objects.requireNonNull(profilesEntityManagerFactory.getObject()));
    }


}
