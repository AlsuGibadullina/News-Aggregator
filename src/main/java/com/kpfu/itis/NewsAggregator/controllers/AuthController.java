package com.kpfu.itis.NewsAggregator.controllers;

import com.kpfu.itis.NewsAggregator.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> users = new HashMap<>();
    private SpringTemplateEngine templateEngine;

    @Autowired
    private AuthService authService;

    public AuthController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        System.out.println("/register");
        authService.registerUser(email, password);

        if (users.containsKey(email)) {
            return "User already exists";
        }
        users.put(email, passwordEncoder.encode(password));
        return "User registered successfully";
    }
}