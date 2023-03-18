package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.responses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnecdoteService {

    private final AnecdoteRepository anecdoteRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public SuccessResponse createAnecdote(CreateAnecdoteRequest request, User user) {

        List<Topic> topics = topicRepository.findAllByNameIn(request.getTopics());
        Anecdote test = new Anecdote(
                request.getContent(),
                user,
                topics
        );
        anecdoteRepository.save(test);

        return SuccessResponse.builder()
                .response("Anecdote created")
                .build();
    }

    public AnecdotesResponse allRandom() {

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

        return AnecdotesResponse.builder()
                .anecdotes(resultAnecdote)
                .build();
    }

    public AnecdotesResponse allFromTopic(AnecdoteFromTopicsRequest request) {
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

        return AnecdotesResponse.builder()
                .anecdotes(resultAnecdote)
                .build();
    }

    public UserAnecdoteResponse allFromUser(User author) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        List<Anecdote> anecdotes = anecdoteRepository.findAllByAuthor(author);
        List<AnecdoteDTO> resultAnecdote = new ArrayList<>();
        for (Anecdote a: anecdotes) {
            if(user.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
            else {
                Optional<Rating> rating = ratingRepository.findByAnecdoteAndUser(a, user.get());
                if(rating.isEmpty()) resultAnecdote.add(new AnecdoteDTO(a));
                else resultAnecdote.add(new AnecdoteDTO(rating.get()));
            }
        }

        return UserAnecdoteResponse.builder()
                .anecdotes(resultAnecdote)
                .author(new UserDTO(user.get()))
                .build();
    }

    public Optional<Anecdote> findById( Integer anecdoteId) {
        return anecdoteRepository.findById(anecdoteId);
    }

}
