package com.andrew.profile_creator.web.mappers;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.dto.response.AppUserResponseDTO;
import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.roles.RoleTypes;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.andrew.profile_creator.utills.UserUtils.roleTypeCheck;

@Component
@SuppressWarnings("ClassCanBeRecord")
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

        return new AppUserResponseDTO(userId, email, name, createdAt, roles);
    }

    public AppUser toEntity(AppUserWriteRequestDTO appUserDTO) throws  RoleTypeNotFoundException{
        AppUser appUser =  new AppUser(
                null,
                appUserDTO.getEmail(),
                appUserDTO.getName(),
                appUserDTO.getPassword(),
                LocalDateTime.now(),
                new ArrayList<>(),
                false,
                false);

        appUserDTO.getRoles().stream().forEach(roleName -> {
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
