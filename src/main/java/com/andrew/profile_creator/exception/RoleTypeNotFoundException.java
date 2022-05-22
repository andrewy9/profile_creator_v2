package com.andrew.profile_creator.exception;

public class RoleTypeNotFoundException extends Exception {

    private final String roleName;

    public RoleTypeNotFoundException(String message, String roleName) {
        super(message);
        this.roleName = roleName;
    }

    public RoleTypeNotFoundException(String message, Throwable cause, String roleName) {
        super(message, cause);
        this.roleName = roleName;
    }

    public String getRoleName() {return roleName;}
}
