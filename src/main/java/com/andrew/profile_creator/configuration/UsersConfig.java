package com.andrew.profile_creator.configuration;

import com.andrew.profile_creator.models.accounts.AppUser;
import com.andrew.profile_creator.models.accounts.Role;
import com.andrew.profile_creator.security.authorization.RoleTypes;
import com.andrew.profile_creator.services.user.AppUserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.andrew.profile_creator.security.authorization.RoleTypes.ADMIN;
import static com.andrew.profile_creator.security.authorization.RoleTypes.USER;

@Configuration

public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner(AppUserServiceImpl userService) {
        return args -> {
            Arrays.stream(RoleTypes.values()).forEach(
                    roleTypes -> userService.addRole(new Role(roleTypes.getId(), roleTypes.name()))
            );
            userService.saveAppUser(new AppUser(
                    null,
                    "jonny.depp@gmail.com",
                    "John Depp",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.saveAppUser(new AppUser(
                    null,
                    "james.bond@gmail.com",
                    "James Bond",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.saveAppUser(new AppUser(
                    null,
                    "jill.kill@gmail.com",
                    "Jill Kill",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.saveAppUser(new AppUser(
                    null,
                    "jane.dane@gmail.com",
                    "Jane Dane",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.addRoleToUser(1L, USER.name());
            userService.addRoleToUser(2L, USER.name());
            userService.addRoleToUser(3L, ADMIN.name());

        };
    }
}
