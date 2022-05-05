package com.andrew.profile_creator.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @SequenceGenerator(
            name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "users_sequence"
    )
    private Long id;
    private String email;
    private String name;
    private String password;
    private LocalDateTime created_at;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private Boolean locked = false;
    private Boolean enabled = false;

    @Override
    public String toString() {
        return "AppUser{" +
                "id='" + id + '\'' +
                ", created_at=" + created_at +
                ", email='" + email + '\'' +
                '}';
    }
}
