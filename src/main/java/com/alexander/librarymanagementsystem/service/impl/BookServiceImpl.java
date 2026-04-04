package com.alexander.librarymanagementsystem.service.impl;


import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.repository.BookRepository;
import com.alexander.librarymanagementsystem.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooks(String keyword) {

        // If empty - return all books
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }

        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public void toggleAvailability(Long id) {

        Book book = bookRepository.findById(id).orElseThrow();

        // Switch between true or false
        book.setAvailable(!book.isAvailable());

        bookRepository.save(book);
    }
}