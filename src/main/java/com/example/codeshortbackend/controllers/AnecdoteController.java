package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
        List<Anecdote> anecdotes = anecdoteRepository.findAll();
        Collections.shuffle(anecdotes);
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            resultAnecdote.add(new AnecdoteDTO(a));
        }
        return ResponseEntity.ok(new AnecdotesResponse(resultAnecdote));
    }

    @GetMapping("/topics")
    public ResponseEntity<AnecdotesResponse> allFromTopics(
            @RequestBody AnecdoteFromTopicsRequest request
    ) {
        // TODO pagination
        List<Topic> topics = topicRepository.findAllByNameIn(request.getTopics());
        List<Anecdote> anecdotes = anecdoteRepository.findAllByTopicsIn(topics);
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            if(a.getTopics().containsAll(topics)) {
                resultAnecdote.add(new AnecdoteDTO(a));
            }
        }
        return ResponseEntity.ok(new AnecdotesResponse(resultAnecdote));
    }
}
