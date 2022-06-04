package com.andrew.profile_creator.dto.request;

import com.andrew.profile_creator.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class AppUserWriteRequestDTO {
    private String email;
    private String name;
    private String password;
    private Collection<String> roles;
}
