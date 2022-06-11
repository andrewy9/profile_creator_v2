package com.andrew.profile_creator.services.user;

import com.andrew.profile_creator.models.accounts.AppUser;
import com.andrew.profile_creator.models.accounts.Role;

import java.util.List;

public interface AppUserService {

    AppUser saveAppUser(AppUser user);

    void addRole(Role role);

    AppUser addRoleToUser(Long userId, String roleName) throws Exception;

    AppUser removeRoleFromUser(Long userId, String roleName) throws Exception;

    List<AppUser> getAppUsers();

    AppUser getUserByEmail (String email);

    AppUser getUserById (Long userId);

    AppUser updateAppUser(Long userId, AppUser appUser);

    void deleteUser(Long userId);

    void enableAppUser(Long id);

}
