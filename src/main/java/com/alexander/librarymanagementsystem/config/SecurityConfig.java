package com.alexander.librarymanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// authentication & authorization rules

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/register").hasRole("ADMIN")

                        // ADMIN ONLY
                        .requestMatchers("/books/**").hasRole("ADMIN")

                        // PUBLIC
                        .anyRequest().permitAll()
                )

                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/books", true)
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("loginError", "Invalid username or password");
                            response.sendRedirect("/login");
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

}
