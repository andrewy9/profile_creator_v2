package com.andrew.profile_creator.configuration;

import com.andrew.profile_creator.models.Users;
import com.andrew.profile_creator.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner(UsersRepository repository) {
        return args -> {
            Users john = new Users(
                    123L,
                    LocalDate.of(2020, JANUARY, 20),
                    "john@springboot.com"
            );

            Users alex = new Users(
                    124L,
                    LocalDate.of(2021, FEBRUARY, 1),
                    "alex@springboot.com"
            );

            Users sam = new Users(
                    125L,
                    LocalDate.of(2000, JULY, 1),
                    "sam@springboot.com"
            );

            repository.saveAll(
                    List.of(john, alex, sam)
            );
        };
    }
}
