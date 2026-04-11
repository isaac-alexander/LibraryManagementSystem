package com.alexander.librarymanagementsystem;

import com.alexander.librarymanagementsystem.entity.Book;
import com.alexander.librarymanagementsystem.entity.User;
import com.alexander.librarymanagementsystem.repository.BookRepository;
import com.alexander.librarymanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class LibraryManagementSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementSystemApplication.class, args);
    }

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        // create new book for test
        Book book = new Book("PSYCHODRAMA", "DAVE","12345", LocalDate.of(2019, 1, 1), true);
        bookRepository.save(book);

        // ADMIN
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("12345"));
            admin.setRole("ADMIN");

            userRepository.save(admin);
        }

        // STUDENT
        if (userRepository.findByUsername("student") == null) {
            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("12345"));
            student.setRole("STUDENT");

            userRepository.save(student);
        }


    }
}