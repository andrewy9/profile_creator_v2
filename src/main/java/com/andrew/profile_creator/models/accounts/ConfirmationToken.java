package com.andrew.profile_creator.models.accounts;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long token_id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime expires_at;

    private LocalDateTime confirmed_at;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "app_user_id")
    private AppUser appUser;

    public ConfirmationToken(String token,
                             LocalDateTime created_at,
                             LocalDateTime expires_at,
                             AppUser appUser) {
        this.token = token;
        this.created_at = created_at;
        this.expires_at = expires_at;
        this.appUser = appUser;
    }
}
