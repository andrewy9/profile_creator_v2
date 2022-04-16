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
                    LocalDate.of(2022, JULY, 20),
                    "john@springboot.com"
            );

            Users alex = new Users(
                    123L,
                    LocalDate.of(2022, MAY, 1),
                    "alex@springboot.com"
            );

            repository.saveAll(
                    List.of(john, alex)
            );
        };
    }
}
