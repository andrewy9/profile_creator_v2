package com.andrew.profile_creator.web.controllers;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.dto.response.AppUserResponseDTO;
import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.RoleToUserForm;
import com.andrew.profile_creator.services.AuthenticateUserIdService;
import com.andrew.profile_creator.services.UserService;
import com.andrew.profile_creator.util.Identifier;
import com.andrew.profile_creator.web.mappers.AppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
@SuppressWarnings("ClassCanBeRecord")
class UsersController {

    @Autowired
    private AppUserMapper appUserMapper;

    private final AuthenticateUserIdService authenticateUserIdService;
    private final UserService userService;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('users:read')") // or @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<AppUserResponseDTO>> getUsers() {
        List<AppUserResponseDTO> appUserList =  userService.getUsers()
                .stream()
                .map(appUserMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(appUserList);
    }

    @GetMapping(path = "/user")
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') or " +
            "authentication.principal.equals(#userEmail) or " +
            "@authenticateUserIdService.hasId(#userId)"
    )
    public ResponseEntity<AppUserResponseDTO> getUser(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) Long userId
    ) throws Exception {

        // Throw exception if both or neither of the parameters are null
        if (((userEmail == null || userEmail.isBlank()) && userId == null)
                        || (userEmail !=null && !userEmail.isBlank() && userId != null)) {
            throw new Exception("must provide one of the two possible parameters: userEmail or userId");
        }

        if (userId != null) {
            AppUserResponseDTO appUser = appUserMapper.toResponseDto(userService.getUserById(userId));
            return ResponseEntity.ok().body(appUser);
        }

        AppUserResponseDTO appUser = appUserMapper.toResponseDto(userService.getUserByEmail(userEmail));
        return ResponseEntity.ok().body(appUser);
    }

    @PostMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> saveAppUser(@RequestBody AppUserWriteRequestDTO appUserRequestDTO) throws RoleTypeNotFoundException {

        AppUser appUser = appUserMapper.toEntity(appUserRequestDTO);
        userService.addUser(appUser);

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toResponseDto(appUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user").toUriString());

        return ResponseEntity.created(uri).body(appUserResponseDTO);
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

