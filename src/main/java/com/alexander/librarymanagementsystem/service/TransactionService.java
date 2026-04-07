package com.alexander.librarymanagementsystem.service;

import com.alexander.librarymanagementsystem.entity.Transaction;

import java.util.List;

public interface TransactionService {

    // borrow book
    void borrowBook(Long bookId, String username);

    // return book (with user check)
    void returnBook(Long bookId, String username);

    // get books borrowed by a specific user
    List<Transaction> getUserTransactions(String username);

    // get all transactions for admin
    List<Transaction> getAllTransactions();
}