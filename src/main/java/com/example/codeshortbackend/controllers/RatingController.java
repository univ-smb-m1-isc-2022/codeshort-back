package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/anecdote")
@RequiredArgsConstructor
public class RatingController {

    private final AnecdoteRepository anecdoteRepository;
    private final UserRepository userRepository;
    private final RatingService ratingService;

    @PostMapping("/{anecdoteId}/rating")
    public ResponseEntity<?> rate(
            @PathVariable Integer anecdoteId,
            @RequestBody RatingRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        Optional<Anecdote> anecdote = anecdoteRepository.findById(anecdoteId);
        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        return ResponseEntity.ok(ratingService.rate(anecdote.get(),user.get(),request));
    }

    @GetMapping("/starred/{username}")
    public ResponseEntity<?> getStarred(
            @PathVariable String username
    ) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        return ResponseEntity.ok(ratingService.getStarred(user.get()));
    }
}
