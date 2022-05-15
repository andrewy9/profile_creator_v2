package com.andrew.profile_creator.repository.roles;

public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USERS_READ("users:read");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
