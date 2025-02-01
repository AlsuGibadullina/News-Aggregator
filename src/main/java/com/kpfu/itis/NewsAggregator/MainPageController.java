package com.kpfu.itis.NewsAggregator;


import com.kpfu.itis.NewsAggregator.forms.AuthForm;
import com.kpfu.itis.NewsAggregator.models.dtos.NewsDto;
import com.kpfu.itis.NewsAggregator.services.NewsService;
import com.kpfu.itis.NewsAggregator.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final NewsService newsService;

    @GetMapping("/news")
    public String newsPage() {
        return "news_page";
    }

    @GetMapping("/news/detail/{id}")
    public String newsDetailPage(@PathVariable("id") Long id, Model model) {
        // Получаем объект новости по id из сервиса
        NewsDto news = newsService.convertToDto(newsService.getById(id));
        model.addAttribute("news", news);
        return "news_detail"; // Ищется шаблон news_detail.html в папке templates
    }


    @Autowired
    private UserService userService;

//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @GetMapping("/register")
//    public ModelAndView showRegistrationForm() {
//        System.out.println("Регистрация пользователя");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("register");
//        return modelAndView;
//    }
//
//    @GetMapping("/login")
//    public ModelAndView showLoginForm() {
//        System.out.println("Логин пользователя");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@RequestParam String email, @RequestParam String password) {
//        System.out.println("Регистрация пользователя: " + email);
//        userService.registerUser(email, password);
//        System.out.println("Пользователь зарегистрирован, перенаправление на вход");
//        return "Success!";
////        return "redirect:/login";
//    }
//
//    @PostMapping("/perform_login")
//    public String performLoginUser(@ModelAttribute AuthForm loginForm) {
//        System.out.println("/perform_login");
//        System.out.println("Email: " + loginForm.getEmail());
//        System.out.println("Password: " + loginForm.getPassword());
//
//        if (userService.authenticateUser(loginForm.getEmail(), loginForm.getPassword()).isPresent()) {
//            UserDetails userDetails = loadUserByUsername(loginForm.getEmail());
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
//            return "Success!";
//        } else {
//            return "redirect:/login?error=true";
//        }
//    }
//
//
//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute AuthForm loginForm) {
//        System.out.println("Auth user");
//        System.out.println("Email: " + loginForm.getEmail());
//        System.out.println("Password: " + loginForm.getPassword());
//
//        if (userService.authenticateUser(loginForm.getEmail(), loginForm.getPassword()).isPresent()) {
//            return "T";
////            return "redirect:/";
//        } else {
//            return "F";
////            return "redirect:/login?error=true";
//        }
//    }

//    @GetMapping("/confirm")
//    public String confirmUser(@RequestParam String code) {
//        if (userService.confirmUser(code)) {
//            return "redirect:/login?confirmed=true";
//        }
//        return "redirect:/login?error=true";
//    }

//    private UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userService.authenticateUser(email, "")
//                .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), new ArrayList<>()))
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
}
