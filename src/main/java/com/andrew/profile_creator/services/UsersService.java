package com.andrew.profile_creator.services;

import com.andrew.profile_creator.models.Users;
import com.andrew.profile_creator.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getUsers() {
            return usersRepository.findAll();
    }

    public void addNewUser(Users user) {
        Optional<Users> userOptional = usersRepository
                .findUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        usersRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = usersRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "user with id " +  userId + "does not exist");
        }
        usersRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String email) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " +  userId + "does not exist"
                ));

        if (email != null && email.length() > 0)  {
            Optional<Users> userOptional = usersRepository
                    .findUserByEmail(email);
            if(userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }
}
