package com.andrew.profile_creator.repository.profiles;

import com.andrew.profile_creator.models.profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long > {

}
