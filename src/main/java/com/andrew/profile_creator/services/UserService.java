package com.andrew.profile_creator.services;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.util.Identifier;

import java.util.List;

public interface UserService {

    AppUser addUser(AppUser user);

    Role addRole(Role role);

    void addRoleToUser(String email, String roleName) throws RoleTypeNotFoundException;

    void removeRoleFromUser(String email, String roleName);

    List<AppUser> getUsers();

    AppUser getUserByEmail (String email);

    AppUser getUserById (Long userId);

    void updateUser(Long userId, AppUser appUser);

    void deleteUser(Long userId);

    Integer enableAppUser(String email);

}
