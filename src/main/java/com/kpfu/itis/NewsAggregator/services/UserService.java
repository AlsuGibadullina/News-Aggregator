package com.kpfu.itis.NewsAggregator.services;

import com.kpfu.itis.NewsAggregator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User createUser(String email, String passwordHash) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    // и т.д. — методы для обновления пользователя, изменения пароля, и т.п.
}
