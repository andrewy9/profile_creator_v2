package com.andrew.profile_creator.configuration;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.andrew.profile_creator.repository.roles.RoleTypes.*;

@Configuration
public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserServiceImpl userService) {
        return args -> {
            userService.addRole(new Role(null, USER.name()));
            userService.addRole(new Role(null, ADMIN.name()));

            userService.addUser(new AppUser(
                    null,
                    "jonny.depp@gmail.com",
                    "John Depp",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));


            userService.addUser(new AppUser(
                    null,
                    "james.bond@gmail.com",
                    "James Bond",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));

            userService.addUser(new AppUser(
                    null,
                    "jill.kill@gmail.com",
                    "Jill Kill",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.addUser(new AppUser(
                    null,
                    "jane.dane@gmail.com",
                    "Jane Dane",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));

            userService.addRoleToUser("jonny.depp@gmail.com", USER.name());
            userService.addRoleToUser("james.bond@gmail.com", USER.name());
            userService.addRoleToUser("jill.kill@gmail.com", ADMIN.name());

        };
    }
}
