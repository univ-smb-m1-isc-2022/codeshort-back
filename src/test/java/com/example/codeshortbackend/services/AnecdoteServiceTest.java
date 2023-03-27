package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
public class AnecdoteServiceTest {

    private AnecdoteService anecdoteService;
    private AnecdoteRepository anecdoteRepository;
    private TopicRepository topicRepository;
    private AuthenticationService authenticationService;
    private RatingRepository ratingRepository;

    @BeforeEach
    public void init() {
        anecdoteRepository = mock(AnecdoteRepository.class);
        authenticationService = mock(AuthenticationService.class);
        topicRepository = mock(TopicRepository.class);
        ratingRepository = mock(RatingRepository.class);
        anecdoteService = new AnecdoteService(anecdoteRepository, topicRepository, authenticationService, ratingRepository);
    }

    @Test
    public void allFromTopic_shouldHaveTwo() {

        Topic topic1 = Topic.builder().name("topic1").build();
        Topic topic2 = Topic.builder().name("topic2").build();
        ArrayList<Topic> topics = new ArrayList<>();
        topics.add(topic1);
        ArrayList<Topic> topicsFalse = new ArrayList<>();
        topicsFalse.add(topic2);

        User user = User.builder().username("username").build();
        Anecdote anecdote1 = Anecdote.builder().author(user).content("content1").topics(topics).build();
        Anecdote anecdote2 = Anecdote.builder().author(user).content("content2").topics(topics).build();
        Anecdote anecdote3 = Anecdote.builder().author(user).content("content3").topics(topicsFalse).build();
        ArrayList<Anecdote> anecdotes = new ArrayList<>();
        anecdotes.add(anecdote1);
        anecdotes.add(anecdote2);
        anecdotes.add(anecdote3);

        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(topicRepository.findAllByNameIn(any())).thenReturn(topics);
        when(anecdoteRepository.findAllByTopicsIn(any())).thenReturn(anecdotes);

        ArrayList<AnecdoteDTO> anecdotesDTO = new ArrayList<>();
        anecdotesDTO.add(new AnecdoteDTO(anecdote1));
        anecdotesDTO.add(new AnecdoteDTO(anecdote2));

        Assertions.assertEquals(
                AnecdotesResponse.builder().anecdotes(anecdotesDTO).build(),
                anecdoteService.allFromTopic(AnecdoteFromTopicsRequest.builder().build())
        );

    }
}
