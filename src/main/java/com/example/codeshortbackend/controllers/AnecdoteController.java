package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.responses.AnecdoteFromTopicsResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/anecdote")
@RequiredArgsConstructor
public class AnecdoteController {

    private final AnecdoteRepository anecdoteRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> createAnecdote(
            @RequestBody CreateAnecdoteRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        // TODO catch exception
        anecdoteRepository.save(
                new Anecdote(
                    request.getContent(),
                    user
                )
            );
        return ResponseEntity.ok(new SuccessResponse("Anecdote created"));
    }

    @GetMapping("/topics")
    public ResponseEntity<AnecdoteFromTopicsResponse> allFromTopics(
            @RequestBody AnecdoteFromTopicsRequest request
    ) {
        List<Topic> topics = topicRepository.findAllByNameIn(request.getTopics());
        List<Anecdote> anecdotes = anecdoteRepository.findAllByTopics(topics);
        return ResponseEntity.ok(new AnecdoteFromTopicsResponse(anecdotes));
    }
}
