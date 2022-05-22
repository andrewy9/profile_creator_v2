package com.andrew.profile_creator.exception;


public class UserDoesNotExistException extends Exception {

    private final String username;

    public UserDoesNotExistException(String message, String username) {
        super(message);
        this.username = username;
    }

    public UserDoesNotExistException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }

    public String getUsername() {return username;}
}
