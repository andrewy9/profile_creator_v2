package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.RoleRepository;
import com.andrew.profile_creator.repository.UserRepository;
import com.andrew.profile_creator.repository.roles.RoleTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final static String USER_NOT_FOUND_MESSAGE = "user with email %s not found";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));

        if(appUser == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        appUser.getRoles().forEach(role -> {
            RoleTypes.valueOf(role.getName()).getGrantedAuthoritiesStr().forEach(perm -> {
                authorities.add(new SimpleGrantedAuthority(perm));
            });
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                authorities
        );
    }

    @Override
    public List<AppUser> getUsers() {
            return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalStateException("user does not exist");
        }
        return userOptional.get();
    }

    @Override
    public AppUser getUserByEmail(String email){
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            throw new IllegalStateException("user does not exist");
        }
        return userOptional.get();
    }

    @Override
    public AppUser addUser(AppUser user) {
        Optional<AppUser> userOptional = userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, String email) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " +  userId + "does not exist"
                ));

        if (email != null && email.length() > 0)  {
            Optional<AppUser> userOptional = userRepository
                    .findByEmail(email);
            if(userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }

    @Override
    public Role addRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding a role {} to user {}", roleName, email);
        Optional<AppUser> user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        if (user.isPresent()) {
            user.get().getRoles().add(role);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " +  userId + "does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public Integer enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

}
