package com.andrew.profile_creator.util.web.controllers;

import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.RoleToUserForm;
import com.andrew.profile_creator.services.AuthenticateUserIdService;
import com.andrew.profile_creator.services.UserService;
import com.andrew.profile_creator.util.Identifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
@SuppressWarnings("ClassCanBeRecord")
class UsersController {

    private final UserService userService;

    private final AuthenticateUserIdService authenticateUserIdService;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('users:read')") // or @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/user")
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') or " +
            "authentication.principal.equals(#userEmail) or " +
            "@authenticateUserIdService.hasId(#userId)"
    )
    public ResponseEntity<AppUser> getUser(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) Long userId
    ) throws Exception {

        // Throw exception if both or neither of the parameters are null
        if (((userEmail == null || userEmail.isBlank()) && userId == null)
                        || (userEmail !=null && !userEmail.isBlank() && userId != null)) {
            throw new Exception("must provide one of the two possible parameters: userEmail or userId");
        }

        if (userId != null) {
            return ResponseEntity.ok().body(userService.getUserById(userId));
        }

        return ResponseEntity.ok().body(userService.getUserByEmail(userEmail));
    }

    @PostMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> saveAppUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user").toUriString());
        return ResponseEntity.created(uri).body(userService.addUser(user));
    }

    @PutMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.equals(#userEmail)")
    public ResponseEntity<?> updateUserById(@RequestParam(required = false) Long userId,
                                            @RequestParam(required = false) String userEmail,
                                            @RequestBody AppUser appUser) throws Exception {

        if (((userEmail == null || userEmail.isBlank()) && userId == null)
                || (userEmail !=null && !userEmail.isBlank() && userId != null)) {
            throw new Exception("must provide one of the two possible parameters: userEmail or userId");
        }

        if (userId != null) {
            userService.updateUser(userId, appUser);
            return ResponseEntity.ok().build();
        }

        userService.updateUser(
            userService.getUserByEmail(userEmail).getId(), appUser
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/role/addToUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) throws RoleTypeNotFoundException {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/by_id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUserById(@RequestParam("userId") Long id) {
        userService.deleteUser(id);
    }


}

