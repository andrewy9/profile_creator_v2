package com.andrew.profile_creator.repository;

import com.andrew.profile_creator.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsersRepository
        extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Optional<Users> findUserByEmail(String email);
}