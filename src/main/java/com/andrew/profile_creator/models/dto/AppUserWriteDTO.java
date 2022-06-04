package com.andrew.profile_creator.models.dto;

import com.andrew.profile_creator.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class AppUserCreationDTO {
    private String email;
    private String name;
    private String password;
    private LocalDateTime created_at;
    private Collection<Role> roles;
}
