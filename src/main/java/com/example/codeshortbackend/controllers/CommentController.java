package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.CommentRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.responses.CommentDTO;
import com.example.codeshortbackend.responses.CommentsResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/anecdote/{anecdoteId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final AnecdoteRepository anecdoteRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @PostMapping("")
    public ResponseEntity<?> createComment(
            @PathVariable Integer anecdoteId,
            @RequestBody CreateCommentRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Optional<Anecdote> anecdote = anecdoteRepository.findById(anecdoteId);

        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        Comment comment = new Comment(user, anecdote.get(), request.getContent());
        commentRepository.save(comment);
        return ResponseEntity.ok(new SuccessResponse("Comment created"));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all(@PathVariable Integer anecdoteId) {

        Optional<Anecdote> anecdote = anecdoteRepository.findById(anecdoteId);
        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The anecdote doesn't exist");
        }

        List<CommentDTO> comments = new ArrayList<>();

        for (Comment c: anecdote.get().getComments()) {
            comments.add(new CommentDTO(c));
        }
        return ResponseEntity.ok(new CommentsResponse(comments));
    }
}
