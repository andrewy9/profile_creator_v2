package com.andrew.profile_creator.util.web.controllers;

import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.RoleToUserForm;
import com.andrew.profile_creator.services.AuthenticateUserIdService;
import com.andrew.profile_creator.services.UserService;
import com.andrew.profile_creator.util.Identifier;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
class UsersController {

    private final UserService userService;

    private final AuthenticateUserIdService authenticateUserIdService;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('users:read')") // or @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/user")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.equals(#userEmail)")
    @PreAuthorize("@authenticateUserIdService.hasId(#userId)")
    public ResponseEntity<AppUser> getUserByEmail(
            @NotNull @RequestParam(required = false) String userEmail,
            @NotNull @RequestParam(required = false) Long userId
    ) throws Exception {

        if (userEmail == null || userEmail.isBlank()) {
           if(userId == null) {
               throw new Exception("both email or userId cannot be empty");
           }
            return ResponseEntity.ok().body(userService.getUserById(userId));
        }

        if (userId != null) {
            throw new Exception("both email or userId cannot have values");
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

        Identifier identifier;
        if (userEmail == null || userEmail.isBlank()) {
            if(userId == null) {
                throw new Exception("both email or userId cannot be empty");
            }
            Optional<Long> userIdOptional = Optional.ofNullable(userId);
            identifier = new Identifier(userIdOptional, null);
        } else {
            if (userId != null) {
                throw new Exception("both email or userId cannot have values");
            }
            Optional<String> userEmailOptional = Optional.ofNullable(userEmail);
            identifier = new Identifier(null, userEmailOptional);
        }
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
