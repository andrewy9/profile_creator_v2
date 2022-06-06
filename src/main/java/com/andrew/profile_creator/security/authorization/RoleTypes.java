package com.andrew.profile_creator.security.authorization;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.andrew.profile_creator.security.authorization.UserPermission.*;

public enum RoleTypes {
    USER(1L, Sets.newHashSet(
            USER_READ,
            USER_WRITE
    )),
    ADMIN(2L, Sets.newHashSet(
            USER_READ,
            USER_WRITE,
            USER_DELETE,
            USERS_READ
    ));

    private final Set<UserPermission> permissions;
    private final Long id;

    RoleTypes( Long id, Set<UserPermission> permissions) {
        this.id = id;
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }
    public Long getId() {return id;}

    public Set<SimpleGrantedAuthority>  getGrantedAuthorities() {
       Set<SimpleGrantedAuthority> permissions =  getPermissions().stream().map(permission ->
                new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

       permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

       return permissions;
    }

    public Set<String>  getGrantedAuthoritiesStr() {
        Set<String> permissions =  getPermissions().stream().map(UserPermission::getPermission)
                .collect(Collectors.toSet());

        permissions.add("ROLE_" + this.name());

        return permissions;
    }
}
