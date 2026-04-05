package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// handles login page
@Controller
public class LoginController {

    @GetMapping("/")
    public String defaultRoute() {
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "login";
        } else {
            return "redirect:/books";
        }

    }
}
