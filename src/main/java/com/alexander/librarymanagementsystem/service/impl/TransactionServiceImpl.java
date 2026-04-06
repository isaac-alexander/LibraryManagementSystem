package com.alexander.librarymanagementsystem.service.impl;

import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.entity.User;
import com.alexander.librarymanagementsystem.repository.BookRepository;
import com.alexander.librarymanagementsystem.repository.TransactionRepository;
import com.alexander.librarymanagementsystem.repository.UserRepository;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final BookRepository bookRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(BookRepository bookRepository,
                                  TransactionRepository transactionRepository,
                                  UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // borrow book
    @Override
    public void borrowBook(Long bookId, String username) {

        // get book from db
        Book book = bookRepository.findById(bookId).orElseThrow();

        // check if book is already borrowed
        if (!book.isAvailable()) {
            throw new RuntimeException("book already borrowed");
        }

        // get logged-in user
        User user = userRepository.findByUsername(username);

        // create new transaction
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setReturned(false);

        // save transaction
        transactionRepository.save(transaction);

        // mark book unavailable
        book.setAvailable(false);
        bookRepository.save(book);
    }

    // return book
    @Override
    public void returnBook(Long bookId) {

        // get book
        Book book = bookRepository.findById(bookId).orElseThrow();

        // find active transaction
        Transaction transaction = transactionRepository
                .findByBookAndReturnedFalse(book);

        // if no active transaction → just ignore (no crash)
        if (transaction == null) {
            return;
        }

        // update transaction
        transaction.setReturned(true);
        transaction.setReturnDate(LocalDate.now());

        transactionRepository.save(transaction);

        // mark book available again
        book.setAvailable(true);
        bookRepository.save(book);
    }

    // get logged-in user books
    @Override
    public List<Transaction> getUserTransactions(String username) {

        // find user
        User user = userRepository.findByUsername(username);

        // return only this user's transactions
        return transactionRepository.findByUser(user);
    }

    // admin gets all transactions
    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }
}