package com.kpfu.itis.NewsAggregator.services;

import com.kpfu.itis.NewsAggregator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository userRepository;


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
