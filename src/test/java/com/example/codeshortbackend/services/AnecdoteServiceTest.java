package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.AnecdoteReportRepository;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class AnecdoteServiceTest {

    private AnecdoteService anecdoteService;
    private AnecdoteRepository anecdoteRepository;
    private TopicRepository topicRepository;
    private AuthenticationService authenticationService;
    private RatingRepository ratingRepository;
    private AnecdoteReportRepository anecdoteReportRepository;

    @BeforeEach
    public void init() {
        anecdoteRepository = mock(AnecdoteRepository.class);
        authenticationService = mock(AuthenticationService.class);
        topicRepository = mock(TopicRepository.class);
        ratingRepository = mock(RatingRepository.class);
        anecdoteReportRepository = mock(AnecdoteReportRepository.class);
        anecdoteService = new AnecdoteService(anecdoteRepository, topicRepository, authenticationService, ratingRepository, anecdoteReportRepository);
    }

    @Test
    public void allFromTopic_shouldHaveTwo_withUser_withRating() {

        ArrayList<Topic> topics = createTopics1();
        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO();

        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(topicRepository.findAllByNameIn(any())).thenReturn(topics);
        when(anecdoteRepository.findAllByTopicsIn(any())).thenReturn(anecdotes);
        when(ratingRepository.findByAnecdoteAndUser(any(), any())).thenReturn(Optional.of(
                Rating.builder().vote(Vote.NONE).anecdote(createAnecdote1()).build()
        ));

        Assertions.assertEquals(
                AnecdotesResponse.builder().anecdotes(anecdotesDTO).build(),
                anecdoteService.allFromTopic(AnecdoteFromTopicsRequest.builder().build())
        );

    }

    @Test
    public void allFromTopic_shouldHaveTwo_withUser_ratingNotFound() {

        ArrayList<Topic> topics = createTopics1();
        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO();

        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(topicRepository.findAllByNameIn(any())).thenReturn(topics);
        when(anecdoteRepository.findAllByTopicsIn(any())).thenReturn(anecdotes);
        when(ratingRepository.findByAnecdoteAndUser(any(), any())).thenReturn(Optional.empty());

        Assertions.assertEquals(
                AnecdotesResponse.builder().anecdotes(anecdotesDTO).build(),
                anecdoteService.allFromTopic(AnecdoteFromTopicsRequest.builder().build())
        );

    }
    @Test
    public void allFromTopic_shouldHaveTwo_withEmptyUser() {

        ArrayList<Topic> topics = createTopics1();
        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO();

        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(topicRepository.findAllByNameIn(any())).thenReturn(topics);
        when(anecdoteRepository.findAllByTopicsIn(any())).thenReturn(anecdotes);

        Assertions.assertEquals(
                AnecdotesResponse.builder().anecdotes(anecdotesDTO).build(),
                anecdoteService.allFromTopic(AnecdoteFromTopicsRequest.builder().build())
        );

    }

    @Test
    public void allRandom_shouldHaveTwo_withUser_withRating() {

        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO(anecdotes);

        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteRepository.findAll()).thenReturn(anecdotes);
        when(ratingRepository.findByAnecdoteAndUser(any(), any())).thenReturn(Optional.of(
                Rating.builder().vote(Vote.NONE).anecdote(createAnecdote1()).build()
        ));

        AnecdotesResponse wantTo = AnecdotesResponse.builder().anecdotes(anecdotesDTO).build();
        AnecdotesResponse got = anecdoteService.allRandom();

        Assertions.assertTrue(wantTo.getAnecdotes().containsAll(got.getAnecdotes()));

    }

    @Test
    public void allRandom_shouldHaveTwo_withUser_ratingNotFound() {

        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO(anecdotes);

        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteRepository.findAll()).thenReturn(anecdotes);
        when(ratingRepository.findByAnecdoteAndUser(any(), any())).thenReturn(Optional.empty());

        AnecdotesResponse wantTo = AnecdotesResponse.builder().anecdotes(anecdotesDTO).build();
        AnecdotesResponse got = anecdoteService.allRandom();

        Assertions.assertTrue(wantTo.getAnecdotes().containsAll(got.getAnecdotes()));

    }
    @Test
    public void allRandom_shouldHaveTwo_withEmptyUser() {

        ArrayList<Anecdote> anecdotes = createAnecdotes();
        ArrayList<AnecdoteDTO> anecdotesDTO = createAnecdotesDTO(anecdotes);

        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(anecdoteRepository.findAll()).thenReturn(anecdotes);

        AnecdotesResponse wantTo = AnecdotesResponse.builder().anecdotes(anecdotesDTO).build();
        AnecdotesResponse got = anecdoteService.allRandom();

        Assertions.assertTrue(wantTo.getAnecdotes().containsAll(got.getAnecdotes()));

    }


    // ----------------------
    // ------- CREATE -------
    // ----------------------

    public ArrayList<Topic> createTopics1() {
        Topic topic1 = Topic.builder().name("topic1").build();
        ArrayList<Topic> topics = new ArrayList<>();
        topics.add(topic1);
        return topics;
    }
    public ArrayList<Anecdote> createAnecdotes() {

        Topic topic2 = Topic.builder().name("topic2").build();
        ArrayList<Topic> topicsFalse = new ArrayList<>();
        topicsFalse.add(topic2);

        Anecdote anecdote1 = createAnecdote1();
        Anecdote anecdote2 = Anecdote.builder().author(createUser()).content("content1").topics(topicsFalse).build();
        ArrayList<Anecdote> anecdotes = new ArrayList<>();
        anecdotes.add(anecdote1);
        anecdotes.add(anecdote1);
        anecdotes.add(anecdote2);

        return anecdotes;
    }

    public User createUser() {
        return User.builder().username("username").build();
    }
    public Anecdote createAnecdote1() {
        return Anecdote.builder().author(createUser()).content("content1").topics(createTopics1()).build();
    }

    public ArrayList<AnecdoteDTO> createAnecdotesDTO() {
        Anecdote anecdote1 = createAnecdote1();
        ArrayList<AnecdoteDTO> anecdotesDTO = new ArrayList<>();
        anecdotesDTO.add(new AnecdoteDTO(anecdote1));
        anecdotesDTO.add(new AnecdoteDTO(anecdote1));
        return anecdotesDTO;
    }

    public ArrayList<AnecdoteDTO> createAnecdotesDTO(ArrayList<Anecdote> anecdotes) {
        ArrayList<AnecdoteDTO> anecdotesDTO = new ArrayList<>();
        anecdotes.forEach((anecdote -> {
            anecdotesDTO.add(new AnecdoteDTO(anecdote));
        }));
        return anecdotesDTO;
    }
}
