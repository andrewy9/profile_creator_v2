package com.andrew.profile_creator.services.user;

import com.andrew.profile_creator.models.AppUser;
import com.andrew.profile_creator.models.Role;
import com.andrew.profile_creator.repository.RoleRepository;
import com.andrew.profile_creator.repository.UserRepository;
import com.andrew.profile_creator.security.authorization.RoleTypes;
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

import static com.andrew.profile_creator.utills.UserUtils.roleTypeCheck;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
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
            RoleTypes.valueOf(role.getName())
                    .getGrantedAuthoritiesStr()
                    .forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                authorities
        );
    }

    @Override
    public List<AppUser> getAppUsers() {
            return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long userId) {
        Optional<AppUser> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("user does not exist");
        }
        return userOptional.get();
    }

    @Override
    public AppUser getUserByEmail(String email){
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("user does not exist");
        }
        return userOptional.get();
    }

    @Override
    public AppUser saveAppUser(AppUser user) {
        Optional<AppUser> userOptional = userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("email o taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public AppUser updateAppUser(Long userId, AppUser appUserToBe) {

        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + "does not exist"
                ));

        String email = appUserToBe.getEmail();
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        String name = appUserToBe.getName();
        String password = appUserToBe.getPassword();
        Collection<Role> roles = appUserToBe.getRoles();

        appUser.setEmail(email);
        appUser.setName(name);
        appUser.setPassword(password);
        appUser.setRoles(roles);

        return appUser;
    }

    @Override
    public void addRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        roleRepository.save(role);
    }

    @Override
    public AppUser addRoleToUser(Long userId, String roleName) throws Exception {

        log.info("Adding a role {} to userId {}", roleName, userId);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        RoleTypes newRole = roleTypeCheck(roleName);

        if (appUser.getRoles().stream().anyMatch(
                role -> role.getName().equals(roleName))) {
            throw new Exception("the role " + roleName + " already exists in user: " + appUser.getEmail());
        }

        appUser.getRoles().add(new Role(newRole.getId(), newRole.name()));

        return appUser;
    }

    @Override
    public AppUser removeRoleFromUser(Long userId, String roleName) throws Exception {
        log.info("Removing a role {} to user {}", roleName, userId);
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        RoleTypes removingRole = roleTypeCheck(roleName);

        if (appUser.getRoles().stream().noneMatch(
                role -> role.getName().equals(roleName))) {
            throw new Exception("the role " + roleName + " does not exist in user: " + appUser.getEmail());
        }

        appUser.getRoles().remove(new Role(removingRole.getId(), removingRole.name()));

        return appUser;
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
    public void enableAppUser(Long id) {
        userRepository.enableAppUser(id);
    }

}
