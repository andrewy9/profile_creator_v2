package com.andrew.profile_creator.web.controllers;

import com.andrew.profile_creator.models.Users;
import com.andrew.profile_creator.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getUsers() {
        return usersService.getUsers();
    }

    @PostMapping
    public void registerNewUser(@RequestBody Users user) {
        usersService.addNewUser(user);
    }

    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        usersService.deleteUser(id);
    }

    @PutMapping(path="{userId}")
    public void updateUser(@PathVariable("userId") Long id,
                           @RequestParam(required = false) String email) {
        usersService.updateUser(id, email);
    }
}
