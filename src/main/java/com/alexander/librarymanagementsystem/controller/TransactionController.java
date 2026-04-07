package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

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

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Transaction> transactions;
        if (isAdmin) {
            transactions = transactionService.getAllTransactions();
        } else {
            transactions = transactionService.getUserTransactions(username);
        }

        // beginner-friendly overdue check
        boolean hasOverdue = false;
        for (Transaction t : transactions) { // loop to check overdue
            if (!t.isReturned() && t.getDueDate().isBefore(LocalDateTime.now())) {
                hasOverdue = true;
                break;
            }
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("hasOverdue", hasOverdue); // has overdue send to html

        return "my-books"; // thymeleaf page
    }
}