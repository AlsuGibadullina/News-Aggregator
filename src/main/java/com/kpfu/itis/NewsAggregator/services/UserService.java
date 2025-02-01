package com.kpfu.itis.NewsAggregator.services;

import com.kpfu.itis.NewsAggregator.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String email, String password) {
        User user = new User();

        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
//        user.setConfirmationCode(UUID.randomUUID().toString());
        userRepository.saveAndFlush(user);

//        sendConfirmationEmail(user.getEmail(), user.getConfirmationCode());
    }

    private void sendConfirmationEmail(String email, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации");
        message.setText("Для подтверждения регистрации перейдите по ссылке: http://localhost:8080/confirm?code=" + confirmationCode);
        mailSender.send(message);
    }

//    public boolean confirmUser(String confirmationCode) {
//        return userRepository.findByConfirmationCode(confirmationCode)
//                .map(user -> {
//                    user.setConfirmationCode(null);
//                    userRepository.saveAndFlush(user);
//                    return true;
//                })
//                .orElse(false);
//    }

    public Optional<User> authenticateUser(String email, String password) {
        System.out.println("Authenticating user with email: " + email);
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // и т.д. — методы для обновления пользователя, изменения пароля, и т.п.
}
