package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
