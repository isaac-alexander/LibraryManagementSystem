package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

        // create list to hold transactions
        List<Transaction> transactions;

        // get the username of the logged-in user
        String username = authentication.getName();

        boolean isAdmin = false;

    // loop through all authorities
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break; // stop once found
            }
        }


        // if admin - get all transactions
        if (isAdmin) {
            transactions = transactionService.getAllTransactions();
        } else {
            // if not admin - get only user's transactions
            transactions = transactionService.getUserTransactions(username);
        }

        // get only the logged-in user's transactions for overdue check
        List<Transaction> userTransactions = transactionService.getUserTransactions(username);

        // assume no overdue at the beginning
        boolean hasOverdue = false;

        // loop through user's transactions
        for (Transaction t : userTransactions) {

            // check if book is not returned and due date has passed
            if (!t.isReturned() && t.getDueDate().isBefore(LocalDateTime.now())) {

                // user has overdue
                hasOverdue = true;

                // stops the loop
                break;
            }
        }

        // send data to html
        model.addAttribute("transactions", transactions);
        model.addAttribute("hasOverdue", hasOverdue);

        // return page
        return "my-books";
    }
}