package com.andrew.profile_creator.models;

import java.time.LocalDate;

public class User {
    private String id;
    private LocalDate created_at;
    private String email;
    //Password?


    public User() {
    }

    /**
     * Constructor for creating an User instance
     * @param id Id string of the user/account
     * @param created_at Date time of the user creation
     * @param email User email that has been registered for this account, and also to be used as a login
     */
    public User(String id, LocalDate created_at, String email) {
        this.id = id;
        this.created_at = created_at;
        this.email = email;
    }

    /**
     * Constructor for DB to create a user with ID
     */
    public User(LocalDate created_at, String email) {
        this.created_at = created_at;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", email='" + email + '\'' +
                '}';
    }
}
