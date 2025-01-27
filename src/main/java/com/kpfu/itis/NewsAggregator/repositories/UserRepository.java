package com.kpfu.itis.NewsAggregator.repositories;

import com.kpfu.itis.NewsAggregator.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
     //Optional<User> findByConfirmationCode(String confirmationCode);
}