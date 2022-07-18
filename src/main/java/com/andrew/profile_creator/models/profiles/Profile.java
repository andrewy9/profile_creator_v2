package com.andrew.profile_creator.models.profiles;

import com.andrew.profile_creator.models.accounts.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @SequenceGenerator(
            name = "profile_sequence",
            sequenceName = "profile_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "profile_sequence"
    )
    private Long profile_id;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

//    @ManyToMany(fetch = EAGER)
//    private Collection<Employment> employment = new ArrayList<>();

}
