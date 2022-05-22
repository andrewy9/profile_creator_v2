package com.andrew.profile_creator.exception;

public class RoleExistsInUserAssignedRoles extends Exception {

    private final String roleName;
    private final String username;

    public RoleExistsInUserAssignedRoles(String message, String roleName, String username) {
        super(message);
        this.roleName = roleName;
        this.username = username;
    }

    public RoleExistsInUserAssignedRoles(String message, Throwable cause, String roleName, String username) {
        super(message, cause);
        this.roleName = roleName;
        this.username = username;
    }

    public String getRoleName() {return roleName;}
    public String getUsername() {return username;}
}
