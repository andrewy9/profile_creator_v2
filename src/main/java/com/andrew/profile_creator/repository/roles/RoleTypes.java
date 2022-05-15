package com.andrew.profile_creator.repository.roles;

import com.andrew.profile_creator.models.Role;
import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.andrew.profile_creator.repository.roles.UserPermission.*;

public enum RoleTypes {
    USER(Sets.newHashSet(
            USER_READ,
            USER_WRITE
    )),
    ADMIN(Sets.newHashSet(
            USER_READ,
            USER_WRITE,
            USERS_READ
    ));

    private final Set<UserPermission> permissions;

    RoleTypes(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority>  getGrantedAuthorities() {
       Set<SimpleGrantedAuthority> permissions =  getPermissions().stream().map(permission ->
                new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

       permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

       return permissions;
    }

    public Set<String>  getGrantedAuthoritiesStr() {
        Set<String> permissions =  getPermissions().stream().map(permission ->
                        permission.getPermission())
                .collect(Collectors.toSet());

        permissions.add("ROLE_" + this.name());

        return permissions;
    }
}
