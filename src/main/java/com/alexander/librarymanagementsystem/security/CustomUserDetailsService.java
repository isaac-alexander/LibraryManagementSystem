package com.alexander.librarymanagementsystem.security;

import com.alexander.librarymanagementsystem.entity.User;
import com.alexander.librarymanagementsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
        import org.springframework.stereotype.Service;

import java.util.Collections;

// Connects Spring Security to User table
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Loads user from database during login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(() -> "ROLE_" + user.getRole())
        );
    }
}