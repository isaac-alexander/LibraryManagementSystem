package com.alexander.librarymanagementsystem.service.impl;

import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.entity.User;
import com.alexander.librarymanagementsystem.repository.BookRepository;
import com.alexander.librarymanagementsystem.repository.TransactionRepository;
import com.alexander.librarymanagementsystem.repository.UserRepository;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        // get user
        User user = userRepository.findByUsername(username);

        // get book
        Book book = bookRepository.findById(bookId).orElseThrow();

        // check if book is available
        if (!book.isAvailable()) {
            throw new RuntimeException("book already borrowed");
        }

        // create transaction
        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);

        // store borrow date + time
        LocalDateTime now = LocalDateTime.now();
        transaction.setBorrowDate(now);

        // due date (7 days)
        transaction.setDueDate(now.plusMinutes(5));
        transaction.setReturned(false);

        // save transaction
        transactionRepository.save(transaction);

        // mark book unavailable
        book.setAvailable(false);
        bookRepository.save(book);
    }

    // return book
    @Override
    public void returnBook(Long bookId, String username) {

        // get book
        Book book = bookRepository.findById(bookId).orElseThrow();

        Transaction transaction = transactionRepository.findByBookAndReturnedFalse(book);

        // if no transaction → do nothing
        if (transaction == null) {
            throw new RuntimeException("No active transaction for this book");
        }

        // check if this user is the owner
        boolean isOwner = transaction.getUser().getUsername().equals(username);

        // check if admin
        User user = userRepository.findByUsername(username);
        boolean isAdmin = user.getRole().equals("ADMIN");

        // only owner or admin can return
        if (!isOwner && !isAdmin) {
            throw new RuntimeException("you cannot return this book");
        }

        // update transaction
        transaction.setReturned(true);
        transaction.setReturnDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        // mark book available
        book.setAvailable(true);
        bookRepository.save(book);
    }

    // get user transactions
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