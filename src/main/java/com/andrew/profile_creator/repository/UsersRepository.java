package com.andrew.profile_creator.repository;

import com.andrew.profile_creator.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository
        extends JpaRepository<Users, Long> {

}