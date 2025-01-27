package com.kpfu.itis.NewsAggregator;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class MainPageController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
