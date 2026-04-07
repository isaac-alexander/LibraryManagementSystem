package com.alexander.librarymanagementsystem.repository;

import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // find active borrow for a book
    Transaction findByBookAndReturnedFalse(Book book);

    // find all transactions for a user
    List<Transaction> findByUser(User user);

}
