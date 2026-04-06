package com.alexander.librarymanagementsystem.service;

import com.alexander.librarymanagementsystem.entity.Transaction;

import java.util.List;

public interface TransactionService {

    void borrowBook(Long bookId, String username);

    void returnBook(Long bookId);

    // get books borrowed by a specific user
    List<Transaction> getUserTransactions(String username);

    // get all transactions for admin
    List<Transaction> getAllTransactions();
}