package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;

import java.util.List;

public interface UserService {
    AppUser saveAppUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToAppUser(String username, String role);
    AppUser getAppUser(String username);
    List<AppUser> getAppUsers();
}
