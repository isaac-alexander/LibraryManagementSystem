package com.alexander.librarymanagementsystem.controller;

import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.entity.Transaction;
import com.alexander.librarymanagementsystem.service.BookService;
import com.alexander.librarymanagementsystem.service.TransactionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final TransactionService transactionService;

    public BookController(BookService bookService, TransactionService transactionService) {
        this.bookService = bookService;
        this.transactionService = transactionService;
    }

    // show all books
    @GetMapping
    public String listBooks(Model model, Authentication authentication) {

        model.addAttribute("books", bookService.getAllBooks());

        // get logged-in username
        String username = authentication.getName();

        // get user transactions
        List<Transaction> transactions = transactionService.getUserTransactions(username);

        // store book ids user borrowed
        List<Long> myBookIds = new ArrayList<>();

        for (Transaction t : transactions) {
            // only active borrowed books
            if (!t.isReturned()) {
                myBookIds.add(t.getBook().getId());
            }
        }

        model.addAttribute("myBookIds", myBookIds);

        return "books";
    }

    // create form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    // save book
    @PostMapping("/new")
    public String saveBook(@Valid @ModelAttribute Book book,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "book-form";
        }

        // ensure book is available when created
        book.setAvailable(true);

        bookService.saveBook(book);
        return "redirect:/books";
    }

    // edit form
    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable Long id, Model model) {

        Book book = bookService.getBookById(id);

        model.addAttribute("book", book);

        return "edit-book";
    }

    // update
    @PutMapping("/{id}/edit")
    public String updateBook(@PathVariable Long id, @Valid @ModelAttribute Book book, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/books/{id}/edit";
        }

        Book existingBook = bookService.getBookById(id);

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setYear(book.getYear());
        existingBook.setAvailable(true);

        bookService.saveBook(book);

        return "redirect:/books";
    }

    // delete
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // search
    @GetMapping("/search")
    public String searchBooks(@RequestParam("keyword") String keyword, Model model) {

        List<Book> results = bookService.searchBooks(keyword);

        model.addAttribute("books", results);
        model.addAttribute("keyword", keyword);

        if (keyword != null && !keyword.trim().isEmpty()) {

            // added: search result message
            int count = results.size();

            if (count == 0) {
                model.addAttribute("error", "no books found");
            } else {
                model.addAttribute("success", count + (count == 1 ? " book found" : " books found"));
            }

        }

        return "books";
    }

    // borrow book
    @PostMapping("/{id}/borrow")
    public String borrowBook(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        transactionService.borrowBook(id, username);

        // added success message
        redirectAttributes.addFlashAttribute("success", "book borrowed, go to my books");

        return "redirect:/books";
    }

    // return book
    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        try {
            transactionService.returnBook(id, username);
            redirectAttributes.addFlashAttribute("success", "book returned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/books";
    }
}