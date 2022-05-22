package com.andrew.profile_creator.configuration;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.roles.RoleTypes;
import com.andrew.profile_creator.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.andrew.profile_creator.repository.roles.RoleTypes.ADMIN;
import static com.andrew.profile_creator.repository.roles.RoleTypes.USER;

@Configuration
public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserServiceImpl userService) {
        return args -> {

            Arrays.stream(RoleTypes.values()).forEach(roleTypes -> {
                userService.addRole(new Role(roleTypes.getId(), roleTypes.name()));
            });

            userService.addUser(new AppUser(
                    null,
                    "jonny.depp@gmail.com",
                    "John Depp",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<Role>(),
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
