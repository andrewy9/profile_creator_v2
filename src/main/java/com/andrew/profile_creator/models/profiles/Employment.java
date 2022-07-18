package com.andrew.profile_creator.models.profiles;

import com.andrew.profile_creator.models.accounts.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.MonthDay;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employment_id;
    private String name;
    private MonthDay employment_start;
    private MonthDay employment_end;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

}
