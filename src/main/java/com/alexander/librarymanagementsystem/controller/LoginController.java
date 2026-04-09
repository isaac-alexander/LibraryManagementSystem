package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// handles login page
@Controller
public class LoginController {

    @GetMapping("/")
    public String defaultRoute() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model, Authentication authentication) {

        Object error = session.getAttribute("loginError");

        if (authentication != null) {
            return "redirect:/books";
        }

        if (error != null) {
            model.addAttribute("loginError", error);
            session.removeAttribute("loginError");
        }

        return "login";

    }

}
