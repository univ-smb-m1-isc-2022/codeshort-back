package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.requests.RatingCommentRequest;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/anecdote/{anecdoteId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final AnecdoteService anecdoteService;
    private final AuthenticationService authenticationService;
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<?> createComment(
            @PathVariable Integer anecdoteId,
            @RequestBody CreateCommentRequest request
    ) {
        Optional<User> user = authenticationService.findUser();
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        Optional<Anecdote> anecdote = anecdoteService.findById(anecdoteId);
        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Anecdote not found");
        }

        return ResponseEntity.ok(commentService.createComment(user.get(),anecdote.get(),request));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all(@PathVariable Integer anecdoteId) {

        Optional<Anecdote> anecdote = anecdoteService.findById(anecdoteId);
        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        return ResponseEntity.ok(commentService.all(anecdote.get()));
    }

    @PostMapping("/{commentId}/rating")
    public ResponseEntity<?> rate(
            @PathVariable Integer commentId,
            @RequestBody RatingCommentRequest request
    ) {
        Optional<User> user = authenticationService.findUser();
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        Optional<Comment> comment = commentService.findById(commentId);
        if(comment.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The comment doesn't exist");
        }

        return ResponseEntity.ok(commentService.rate(comment.get(),user.get(),request));
    }
}
