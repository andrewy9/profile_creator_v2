package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.RoleRepository;
import com.andrew.profile_creator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<AppUser> getUsers() {
            return userRepository.findAll();
    }

    public AppUser addNewUser(AppUser user) {
        Optional<AppUser> userOptional = userRepository
                .findUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " +  userId + "does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String email) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " +  userId + "does not exist"
                ));

        if (email != null && email.length() > 0)  {
            Optional<AppUser> userOptional = userRepository
                    .findUserByEmail(email);
            if(userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }

    public Role addRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email, String roleName) {
        log.info("Adding a role {} to user {}", roleName, email);
        Optional<AppUser> user = userRepository.findUserByEmail(email);
        Role role = roleRepository.findByName(roleName);
        if (user.isPresent()) {
            user.get().getRoles().add(role);
        }
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
