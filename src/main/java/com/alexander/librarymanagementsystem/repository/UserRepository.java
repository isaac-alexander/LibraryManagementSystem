package com.alexander.librarymanagementsystem.repository;

import com.alexander.librarymanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // find user by username
    User findByUsername(String username);

}
