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
import com.example.codeshortbackend.responses.*;
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

    private final AnecdoteRepository anecdoteRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final RatingRepository ratingRepository;

    @PostMapping("")
    public ResponseEntity<SuccessResponse> createAnecdote(
            @RequestBody CreateAnecdoteRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Topic> topics = topicRepository.findAllByNameIn(request.getTopics());
        User user = userRepository.findByUsername(username).orElseThrow();
        // TODO catch exception
        Anecdote test = new Anecdote(
                request.getContent(),
                user,
                topics
        );
        anecdoteRepository.save(test);
        return ResponseEntity.ok(new SuccessResponse("Anecdote created"));
    }

    @GetMapping("/random")
    public ResponseEntity<AnecdotesResponse> allRandom() {
        // TODO pagination
        // TODO prendre les anecdotes de la semaine
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        List<Anecdote> anecdotes = anecdoteRepository.findAll();
        Collections.shuffle(anecdotes);
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            if(user.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
            else {
                Optional<Rating> rating = ratingRepository.findByAnecdoteAndUser(a, user.get());
                if(rating.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
                else resultAnecdote.add(new AnecdoteDTO(rating.get()));
            }
        }
        return ResponseEntity.ok(new AnecdotesResponse(resultAnecdote));
    }

    @PostMapping("/topics")
    public ResponseEntity<AnecdotesResponse> allFromTopics(
            @RequestBody AnecdoteFromTopicsRequest request
    ) {
        // TODO pagination
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        List<Topic> topics = topicRepository.findAllByNameIn(request.getTopics());
        List<Anecdote> anecdotes = anecdoteRepository.findAllByTopicsIn(topics);
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            if(a.getTopics().containsAll(topics)) {
                if(user.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
                else {
                    Optional<Rating> rating = ratingRepository.findByAnecdoteAndUser(a, user.get());
                    if(rating.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
                    else resultAnecdote.add(new AnecdoteDTO(rating.get()));
                }
            }
        }
        return ResponseEntity.ok(new AnecdotesResponse(resultAnecdote));
    }

    @GetMapping("/user/{authorName}")
    public ResponseEntity<?> allFromUser(
            @PathVariable String authorName
    ) {
        // TODO pagination
        Optional<User> author = userRepository.findByUsername(authorName);

        if(author.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user doesn't exist");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        List<Anecdote> anecdotes = anecdoteRepository.findAllByAuthor(author.get());
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            if(user.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
            else {
                Optional<Rating> rating = ratingRepository.findByAnecdoteAndUser(a, user.get());
                if(rating.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
                else resultAnecdote.add(new AnecdoteDTO(rating.get()));
            }
        }
        return ResponseEntity.ok(new UserAnecdoteResponse(resultAnecdote, new UserDTO(user.get())));
    }
}
