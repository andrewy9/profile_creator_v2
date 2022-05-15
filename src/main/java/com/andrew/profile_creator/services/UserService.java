package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;

import java.util.List;

public interface UserService {

    AppUser addUser(AppUser user);

    Role addRole(Role role);

    void addRoleToUser(String email, String roleName);

    List<AppUser> getUsers();

    AppUser getUserByEmail (String email);

    AppUser getUserById (Long id);

    void updateUser(Long userId, String email);

    void deleteUser(Long userId);

    Integer enableAppUser(String email);

}
