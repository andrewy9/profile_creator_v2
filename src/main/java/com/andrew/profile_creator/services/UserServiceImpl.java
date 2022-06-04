package com.andrew.profile_creator.services;

import com.andrew.profile_creator.exception.RoleExistsInUserAssignedRoles;
import com.andrew.profile_creator.exception.RoleTypeNotFoundException;
import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.RoleRepository;
import com.andrew.profile_creator.repository.UserRepository;
import com.andrew.profile_creator.repository.roles.RoleTypes;
import com.andrew.profile_creator.util.Identifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@SuppressWarnings("ClassCanBeRecord")
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
    public void updateUser(Long userId, AppUser appUserToBe) {

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + "does not exist"
                ));

        //TODO: Create Request/Response DTO with Mapper
        String email = appUserToBe.getEmail();
        String name = appUserToBe.getName();
        String password = appUserToBe.getPassword();
        if (email != null && email.length() > 0)  {
            Optional<AppUser> userOptional = userRepository
                    .findByEmail(email);
            if(userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
        }
    }

    @Override
    public Role addRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) throws RoleTypeNotFoundException{

        log.info("Adding a role {} to user {}", roleName, email);
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));

        RoleTypes role = Arrays.stream(RoleTypes.values()).filter(roleTypes ->
                roleTypes.name().equals(roleName))
                .findAny()
                .orElseThrow(() -> new RoleTypeNotFoundException("ROLE NOT FOUND", roleName));



        appUser.getRoles().add(new Role( role.getId(), role.name()));
    }

    @Override
    public void removeRoleFromUser(String email, String roleName) {
//        log.info("Removing a role {} to user {}", roleName, email);
//        Optional<AppUser> user = userRepository.findByEmail(email);
//
//        RoleTypes role = Arrays.stream(RoleTypes.values()).filter(roleTypes ->
//                        roleTypes.name().equals(roleName))
//                .findAny()
//                .orElseThrow(() -> new RuntimeException("ROLE NOT FOUND"));
//
//        user.ifPresent(appUser -> appUser.getRoles().remove(role));
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

    private void roleIsUniqueInUserAssignedRolesOrThrowException(AppUser appUser, Role role) throws RoleExistsInUserAssignedRoles {
        if (appUser.getRoles().contains(role)){
            throw new RoleExistsInUserAssignedRoles("Role %s is already assigned to the user %s", appUser.getEmail(), role.getName());
        }
    }

}
