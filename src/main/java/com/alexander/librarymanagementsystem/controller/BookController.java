package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.service.BookService;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final TransactionService transactionService;

    public BookController(BookService bookService, TransactionService transactionService) {
        this.bookService = bookService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    // CREATE FORM
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("formAction", "/books");
        model.addAttribute("method", "post");
        return "book-form";
    }

    // CREATE
    @PostMapping
    public String saveBook(@Valid @ModelAttribute Book book,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("formAction", "/books");
            model.addAttribute("method", "post");
            return "book-form";
        }

        bookService.saveBook(book);
        return "redirect:/books";
    }

    // EDIT FORM
    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {

        Book book = bookService.getBookById(id);

        model.addAttribute("book", book);
        model.addAttribute("formAction", "/books/" + id);
        model.addAttribute("method", "put");

        return "book-form";
    }

    //  UPDATE
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute Book book,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            model.addAttribute("formAction", "/books/" + id);
            model.addAttribute("method", "put");
            return "book-form";
        }

        book.setId(id);
        bookService.saveBook(book);
        return "redirect:/books";
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // SEARCH books
    // GET /books/search?keyword=java
    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword, Model model) {

        model.addAttribute("books", bookService.searchBooks(keyword));
        model.addAttribute("keyword", keyword);

        return "books";
    }

    // Toggle book availability true or false. borrow or return
    @PostMapping("/{id}/toggle")
    public String toggleAvailability(@PathVariable Long id) {

        bookService.toggleAvailability(id);

        return "redirect:/books";
    }

    // borrow book
    @PostMapping("/{id}/borrow")
    public String borrowBook(@PathVariable Long id, Authentication authentication) {

        String username = authentication.getName();

        transactionService.borrowBook(id, username);

        return "redirect:/books";
    }

    // return
    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            transactionService.returnBook(id);
            redirectAttributes.addFlashAttribute("success", "Book returned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unable to return book");
        }

        return "redirect:/books";
    }

}