package com.andrew.profile_creator.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
public class AppUserWriteRequestDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    // Implement @NotBlank in the frontEnd
    private String password;
    // Implement @NotBlank in the frontEnd
    private Collection<String> roles;
}
