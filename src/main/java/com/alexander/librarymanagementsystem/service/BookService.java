package com.alexander.librarymanagementsystem.service;

import com.alexander.librarymanagementsystem.entity.Book;

import java.util.List;

public interface BookService {

    void saveBook(Book book);

    List<Book> getAllBooks();

    Book getBookById(Long id);

    void deleteBook(Long id);

    // search for books
    List<Book> searchBooks(String keyword);

    // toggle availability (borrow/return)
    void toggleAvailability(Long id);

}
