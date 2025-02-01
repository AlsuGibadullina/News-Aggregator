package com.kpfu.itis.NewsAggregator.auth;

import com.kpfu.itis.NewsAggregator.models.entities.User;
import com.kpfu.itis.NewsAggregator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Метод для загрузки пользователя по email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ищем пользователя в БД
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Создаем объект UserDetails с необходимыми данными.
        // Здесь задаем роль "ROLE_USER". При необходимости можно добавить дополнительные роли.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash()) // храните пароль в зашифрованном виде!
                .authorities("ROLE_USER")
                .build();
    }
}
