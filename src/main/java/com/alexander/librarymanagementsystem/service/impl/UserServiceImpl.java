package com.alexander.librarymanagementsystem.service.impl;

import com.alexander.librarymanagementsystem.entity.User;
import com.alexander.librarymanagementsystem.repository.UserRepository;
import com.alexander.librarymanagementsystem.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(User user) {

        // checks if username exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        //  encrypted password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //  default role
        user.setRole("STUDENT");

        userRepository.save(user);
    }
}