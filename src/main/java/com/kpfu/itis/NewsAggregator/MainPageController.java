package com.kpfu.itis.NewsAggregator;

import org.springframework.web.bind.annotation.*;

@RestController
public class MainPageController {

    @GetMapping("/home")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
