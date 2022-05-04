package com.andrew.profile_creator.web.controllers;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.models.RoleToUserForm;
import com.andrew.profile_creator.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/")
@RequiredArgsConstructor
public class UsersController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping(path="/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userServiceImpl.getUsers());
    }

    @PostMapping(path="/user/save")
    public ResponseEntity<AppUser> saveAppUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userServiceImpl.addNewUser(user));
    }

    @PostMapping(path="/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userServiceImpl.addRole(role));
    }

    @DeleteMapping(path="{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userServiceImpl.deleteUser(id);
    }

    @PostMapping(path="/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userServiceImpl.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PutMapping(path="{userId}")
    public void updateUser(@PathVariable("userId") Long id,
                           @RequestParam(required = false) String email) {
        userServiceImpl.updateUser(id, email);
    }
}
