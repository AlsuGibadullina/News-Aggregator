package com.kpfu.itis.NewsAggregator;

import com.kpfu.itis.NewsAggregator.auth.UserService;
import com.kpfu.itis.NewsAggregator.models.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> users = new HashMap<>();
    private SpringTemplateEngine templateEngine;

    @Autowired
    private UserService userService;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        System.out.println("/register");
        userService.registerUser(email, password);

        if (users.containsKey(email)) {
            return "User already exists";
        }
        users.put(email, passwordEncoder.encode(password));
        return "User registered successfully";
    }
}
