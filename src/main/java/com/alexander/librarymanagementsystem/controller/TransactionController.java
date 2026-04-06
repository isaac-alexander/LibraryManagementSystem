package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // student: show only their borrowed books
    // admin: show all transactions
    @GetMapping("/my-books")
    public String myBooks(Authentication authentication, Model model) {

        String username = authentication.getName();

        // check if logged-in user is admin
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // admin sees all transactions
            model.addAttribute("transactions", transactionService.getAllTransactions());
        } else {
            // student sees only their own borrowed books
            model.addAttribute("transactions", transactionService.getUserTransactions(username));
        }

        return "my-books"; // thymeleaf page
    }
}