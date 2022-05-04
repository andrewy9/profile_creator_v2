package com.andrew.profile_creator.configuration;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.services.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static com.andrew.profile_creator.models.RoleEnum.*;

@Configuration
public class UsersConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserServiceImpl userService) {
        return args -> {
            userService.addRole(new Role(null, ROLE_USER.name()));
            userService.addRole(new Role(null, ROLE_MANAGER.name()));
            userService.addRole(new Role(null, ROLE_ADMIN.name()));
            userService.addRole(new Role(null, ROLE_SUPER_ADMIN.name()));

            userService.addNewUser(new AppUser(
                    null,
                    "jonny.depp@gmail.com",
                    "John Travolta",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));


            userService.addNewUser(new AppUser(
                    null,
                    "james.bond@gmail.com",
                    "James Bond",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));

            userService.addNewUser(new AppUser(
                    null,
                    "jill.kill@gmail.com",
                    "Jill Kill",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));
            userService.addNewUser(new AppUser(
                    null,
                    "jane.dane@gmail.com",
                    "Jane Dane",
                    "1234",
                    LocalDateTime.now(),
                    new ArrayList<>(),
                    false,
                    false
            ));

            userService.addRoleToUser("jonny.depp@gmail.com", ROLE_USER.name());
            userService.addRoleToUser("jonny.depp@gmail.com", ROLE_MANAGER.name());
            userService.addRoleToUser("james.bond@gmail.com", ROLE_MANAGER.name());
            userService.addRoleToUser("jill.kill@gmail.com", ROLE_ADMIN.name());
            userService.addRoleToUser("jill.kill@gmail.com", ROLE_ADMIN.name());
            userService.addRoleToUser("jill.kill@gmail.com", ROLE_ADMIN.name());
            userService.addRoleToUser("jill.kill@gmail.com", ROLE_SUPER_ADMIN.name());

        };
    }
}
