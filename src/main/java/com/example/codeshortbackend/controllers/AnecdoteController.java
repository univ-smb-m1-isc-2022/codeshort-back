package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.ReportAnecdoteRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/anecdote")
@RequiredArgsConstructor
public class AnecdoteController {

    private final AnecdoteService anecdoteService;
    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<?> createAnecdote(
            @RequestBody CreateAnecdoteRequest request
    ) {
        Optional<User> user = authenticationService.findUser();
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user doesn't exist");
        }

        return ResponseEntity.ok(anecdoteService.createAnecdote(request, user.get()));
    }

    @GetMapping("/{anecdoteId}")
    public ResponseEntity<?> getOne(
            @PathVariable String anecdoteId
    ) {
        Optional<Anecdote> anecdote = anecdoteService.findById(Integer.parseInt(anecdoteId));
        if(anecdote.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        return ResponseEntity.ok(anecdoteService.getOne(anecdote.get()));
    }

    @GetMapping("/random")
    public ResponseEntity<?> allRandom() {
        return ResponseEntity.ok(anecdoteService.allRandom());
    }

    @GetMapping("/popular")
    public ResponseEntity<?> allPopular() {
        return ResponseEntity.ok(anecdoteService.allPopular());
    }

    @PostMapping("/topics")
    public ResponseEntity<AnecdotesResponse> allFromTopics(
            @RequestBody AnecdoteFromTopicsRequest request
    ) {
        return ResponseEntity.ok(anecdoteService.allFromTopic(request));
    }

    @GetMapping("/user/{authorName}")
    public ResponseEntity<?> allFromUser(
            @PathVariable String authorName
    ) {
        Optional<User> author = userService.findByUsername(authorName);
        if(author.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user doesn't exist");
        }

        return ResponseEntity.ok(anecdoteService.allFromUser(author.get()));
    }

    @PostMapping("/{anecdoteId}/report")
    public ResponseEntity<?> report(
            @PathVariable Integer anecdoteId,
            @RequestBody ReportAnecdoteRequest request
    ) {
        Optional<Anecdote> anecdote = anecdoteService.findById(anecdoteId);
        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        return ResponseEntity.ok(anecdoteService.report(request, anecdote.get()));
    }
}
