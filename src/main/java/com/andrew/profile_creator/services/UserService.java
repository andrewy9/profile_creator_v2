package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;

import java.util.List;

public interface UserService {

    AppUser addUser(AppUser user);

    Role addRole(Role role);

    AppUser addRoleToUser(Long userId, String roleName) throws Exception;

    AppUser removeRoleFromUser(Long userId, String roleName) throws Exception;

    List<AppUser> getUsers();

    AppUser getUserByEmail (String email);

    AppUser getUserById (Long userId);

    AppUser updateUser(Long userId, AppUser appUser);

    void deleteUser(Long userId);

    Integer enableAppUser(String email);

}
