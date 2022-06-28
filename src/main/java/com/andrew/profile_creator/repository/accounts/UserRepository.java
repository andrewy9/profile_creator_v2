package com.andrew.profile_creator.repository.accounts;

import com.andrew.profile_creator.models.accounts.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.user_id = ?1")
    int enableAppUser(Long userId);
}