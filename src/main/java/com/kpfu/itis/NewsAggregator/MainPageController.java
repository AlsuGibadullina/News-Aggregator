package com.kpfu.itis.NewsAggregator;


import com.kpfu.itis.NewsAggregator.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainPageController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/secured")
    @ResponseBody
    public String secured() {
        return "This is a secured resource!";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password) {
        userService.registerUser(email, password);
        return "redirect:/login";
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam String code) {
        if (userService.confirmUser(code)) {
            return "redirect:/login?confirmed=true";
        }
        return "redirect:/login?error=true";
    }
}
