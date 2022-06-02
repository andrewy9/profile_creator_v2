package com.andrew.profile_creator.services;

import com.andrew.profile_creator.exception.UserDoesNotExistException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserIdService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasId(Long userId) throws UserDoesNotExistException {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        AppUser appUser = userRepository.findByEmail(username).orElseThrow(
                ()-> new UserDoesNotExistException("User not found", username)
        );

        return appUser.getId().equals(userId);
    }
}
