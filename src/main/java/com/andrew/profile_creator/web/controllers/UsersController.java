package com.andrew.profile_creator.web.controllers;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.dto.response.AppUserResponseDTO;
import com.andrew.profile_creator.models.accounts.AppUser;
import com.andrew.profile_creator.services.user.AppUserService;
import com.andrew.profile_creator.security.AuthenticateUserIdService;
import com.andrew.profile_creator.web.mappers.AppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.andrew.profile_creator.utills.UserUtils.oneOfIdOrEmailIsProvidedOrThrowException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
class UsersController {

    @Autowired
    private AppUserMapper appUserMapper;

    private final AuthenticateUserIdService authenticateUserIdService;
    private final AppUserService appUserService;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAuthority('users:read')") // or @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<AppUserResponseDTO>> getUsers() {
        List<AppUserResponseDTO> appUserList =  appUserService.getAppUsers()
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

        oneOfIdOrEmailIsProvidedOrThrowException(userId, userEmail);

        if (userId != null) {
            AppUserResponseDTO appUser = appUserMapper.toResponseDto(appUserService.getUserById(userId));
            return ResponseEntity.ok().body(appUser);
        }

        AppUserResponseDTO appUser = appUserMapper.toResponseDto(appUserService.getUserByEmail(userEmail));
        return ResponseEntity.ok().body(appUser);
    }

    @PostMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> saveAppUser(@Valid @RequestBody AppUserWriteRequestDTO appUserRequestDTO) {

        AppUser appUser = appUserService.saveAppUser(appUserMapper.toEntity(appUserRequestDTO));

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toResponseDto(appUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user").toUriString());

        return ResponseEntity.created(uri).body(appUserResponseDTO);
    }

    @PutMapping(path = "/user")
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.equals(#userEmail)")
    public ResponseEntity<AppUserResponseDTO> updateUser(@RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) String userEmail,
                                                         @Valid @RequestBody AppUserWriteRequestDTO appUserRequestDTO) throws Exception {

        oneOfIdOrEmailIsProvidedOrThrowException(userId, userEmail);

        AppUser appUser = appUserMapper.toEntity(appUserRequestDTO);

        AppUser updatedUser;
        if (userId != null) {
            updatedUser = appUserService.updateAppUser(userId, appUser);
        } else {
            Long newUserId = appUserService.getUserByEmail(userEmail).getUser_id();
            updatedUser = appUserService.updateAppUser(newUserId, appUser);
        }

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toResponseDto(updatedUser);
        return ResponseEntity.ok().body(appUserResponseDTO);
    }

    @PutMapping(path = "/user/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> addRoleToUser(@RequestParam(required = false) Long userId,
                                                            @RequestParam(required = false) String userEmail,
                                                            @RequestParam String roleName) throws Exception {

        oneOfIdOrEmailIsProvidedOrThrowException(userId, userEmail);

        AppUser updatedUser;
        if (userId != null) {
            updatedUser = appUserService.addRoleToUser(userId, roleName);
        } else {
            Long findById = appUserService.getUserByEmail(userEmail).getUser_id();
            updatedUser = appUserService.addRoleToUser(findById,roleName);
        }

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toResponseDto(updatedUser);
        return ResponseEntity.ok().body(appUserResponseDTO);
    }

    @PutMapping(path = "/user/removeRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> removeRoleFromUser(@RequestParam(required = false) Long userId,
                                                                 @RequestParam(required = false) String userEmail,
                                                                 @RequestParam String roleName) throws Exception {

        oneOfIdOrEmailIsProvidedOrThrowException(userId, userEmail);

        AppUser updatedUser;
        if (userId != null) {
            updatedUser = appUserService.removeRoleFromUser(userId, roleName);
        } else {
            Long findById = appUserService.getUserByEmail(userEmail).getUser_id();
            updatedUser = appUserService.removeRoleFromUser(findById,roleName);
        }

        AppUserResponseDTO appUserResponseDTO = appUserMapper.toResponseDto(updatedUser);
        return ResponseEntity.ok().body(appUserResponseDTO);
    }

    @DeleteMapping(path = "/user/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUserById(@RequestParam(required = false) Long userId,
                               @RequestParam(required = false) String userEmail) throws Exception {

        oneOfIdOrEmailIsProvidedOrThrowException(userId, userEmail);

        if (userId != null) {
            appUserService.deleteUser(userId);
        } else {
            Long findById = appUserService.getUserByEmail(userEmail).getUser_id();
            appUserService.deleteUser(findById);
        }
    }

}