package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class UserService {

    public List<User> getUsers() {
        return List.of(
                new User(
                        "userNZD",
                        LocalDate.of(2022, Month.JULY, 20),
                        "andrew@springboot.com"
                )
        );
    }

}
