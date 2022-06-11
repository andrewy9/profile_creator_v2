package com.andrew.profile_creator.web.mappers;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.dto.response.AppUserResponseDTO;
import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.accounts.AppUser;
import com.andrew.profile_creator.models.accounts.Role;
import com.andrew.profile_creator.security.authorization.RoleTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.andrew.profile_creator.utills.UserUtils.roleTypeCheck;

@Component
public class AppUserMapper {

    public AppUserResponseDTO toResponseDto(AppUser appUser) {
        Long userId = appUser.getId();
        String email = appUser.getEmail();
        String name = appUser.getName();
        LocalDateTime createdAt = appUser.getCreated_at();
        List<String> roles = appUser
                .getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        Boolean locked = appUser.getLocked();
        Boolean enabled = appUser.getEnabled();

        return new AppUserResponseDTO(userId, email, name, createdAt, roles, locked, enabled);
    }

    public AppUser toEntity(AppUserWriteRequestDTO appUserDTO) {
        AppUser appUser =  new AppUser(
                null,
                appUserDTO.getEmail(),
                appUserDTO.getName(),
                appUserDTO.getPassword(),
                LocalDateTime.now(),
                new ArrayList<>(),
                false,
                false);

        appUserDTO.getRoles().forEach(roleName -> {
            try {
                RoleTypes role = roleTypeCheck(roleName);
                appUser.getRoles().add(new Role(role.getId(), role.name()));
            } catch (RoleTypeNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        return appUser;
    }


}
