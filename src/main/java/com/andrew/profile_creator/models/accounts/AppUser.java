package com.andrew.profile_creator.models.accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
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
    private Long user_id;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private LocalDateTime created_at;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private Boolean locked = false;
    private Boolean enabled = false;
}
