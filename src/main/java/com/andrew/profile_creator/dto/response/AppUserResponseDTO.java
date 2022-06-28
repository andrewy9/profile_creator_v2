package com.andrew.profile_creator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class AppUserResponseDTO {
    private Long user_id;
    private String email;
    private String name;
    private LocalDateTime created_at;
    private Collection<String> roles;
    private Boolean locked;
    private Boolean enabled;
}
