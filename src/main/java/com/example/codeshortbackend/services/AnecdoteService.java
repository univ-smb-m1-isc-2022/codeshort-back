package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.*;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.requests.ReportAnecdoteRequest;
import com.example.codeshortbackend.responses.*;
import lombok.RequiredArgsConstructor;
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
    private final AuthenticationService authenticationService;
    private final RatingRepository ratingRepository;

    private final AnecdoteReportRepository anecdoteReportRepository;

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

        Optional<User> user = authenticationService.findUser();

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

    public AnecdotesResponse allPopular() {

        // TODO pagination
        // TODO prendre les anecdotes de la semaine

        Optional<User> user = authenticationService.findUser();

        List<Anecdote> anecdotes = anecdoteRepository.findTop20ByOrderByUpvotesDesc();
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
        Optional<User> user = authenticationService.findUser();

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
        Optional<User> user = authenticationService.findUser();

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
                .author(new UserDTO(author))
                .build();
    }

    public SuccessResponse report(ReportAnecdoteRequest request, Anecdote anecdote) {

        AnecdoteReport report = AnecdoteReport.builder()
                .anecdote(anecdote)
                .content(request.getContent())
                .build();
        anecdoteReportRepository.save(report);

        return SuccessResponse.builder()
                .response("Anecdote reported")
                .build();
    }

    public AnecdoteResponse getOne( Anecdote anecdote ) {
        AnecdoteDTO result = new AnecdoteDTO(anecdote);
        return AnecdoteResponse.builder().anecdote(result).build();
    }

    public Optional<Anecdote> findById( Integer anecdoteId) {
        return anecdoteRepository.findById(anecdoteId);
    }

}
