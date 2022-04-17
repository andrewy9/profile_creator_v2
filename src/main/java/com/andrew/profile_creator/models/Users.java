package com.andrew.profile_creator.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
public class Users {
    @Id
    @SequenceGenerator(
            name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_sequence"
    )
    private Long id;
    private LocalDate created_at;
    private String email;
    @Transient
    private Integer memberDuration;
    //Password?

    public Users() {
    }

    /**
     * Constructor for creating an Users instance
     * @param id Id string of the user/account
     * @param created_at Date time of the user creation
     * @param email Users email that has been registered for this account, and also to be used as a login
     */
    public Users(Long id, LocalDate created_at, String email) {
        this.id = id;
        this.created_at = created_at;
        this.email = email;
    }

    /**
     * Constructor for DB to create a user with ID
     */
    public Users(LocalDate created_at, String email) {
        this.created_at = created_at;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getMemberDuration() {
        return Period.between(this.created_at, LocalDate.now()).getYears();
    }

    public void setMemberDuration(Integer memberDuration) {
        this.memberDuration = memberDuration;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", email='" + email + '\'' +
                '}';
    }
}
