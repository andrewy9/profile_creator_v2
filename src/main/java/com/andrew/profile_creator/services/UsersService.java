package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.Users;
import com.andrew.profile_creator.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository userRespoistory) {
        this.usersRepository = userRespoistory;
    }

    public List<Users> getUsers() {
            return usersRepository.findAll();
    }

}
