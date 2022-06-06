package com.andrew.profile_creator.web.controllers;

import com.andrew.profile_creator.dto.request.AppUserWriteRequestDTO;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.services.RegistrationService;
import com.andrew.profile_creator.web.mappers.AppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="api/v1/registration")
@RequiredArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AppUserMapper appUserMapper;

    @PostMapping
    public String registerAppUser(@Valid @RequestBody AppUserWriteRequestDTO appUserDto) {
        AppUser userRegistration = appUserMapper.toEntity(appUserDto);
        return registrationService.registerAppUser(userRegistration);
    }

    @GetMapping(path="/confirm")
    public String confirm(@RequestParam String token) {
        return registrationService.confirmToken(token);
    }

}
