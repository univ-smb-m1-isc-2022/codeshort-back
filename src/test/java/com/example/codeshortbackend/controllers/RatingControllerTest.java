package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class RatingControllerTest {
    private AnecdoteService anecdoteService;
    private UserService userService;
    private RatingService ratingService;
    private AuthenticationService authenticationService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        anecdoteService = mock(AnecdoteService.class);
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        ratingService = mock(RatingService.class);
        mockMvc = standaloneSetup(new RatingController(anecdoteService, userService, ratingService, authenticationService)).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void rate_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/anecdote/0/rating")
                .content(asJsonString(RatingRequest.builder()
                        .vote(Vote.NONE)
                        .starred(true)
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void rate_anecdoteShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/anecdote/0/rating")
                .content(asJsonString(RatingRequest.builder()
                        .vote(Vote.NONE)
                        .starred(true)
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void rate_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteService.findById(any())).thenReturn(Optional.of(new Anecdote()));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/anecdote/0/rating")
                .content(asJsonString(RatingRequest.builder()
                        .vote(Vote.NONE)
                        .starred(true)
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getStarred_userShouldBadRequest() throws Exception {
        when(userService.findByUsername(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/anecdote/starred/username")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getStarred_shouldGiveThree() throws Exception {
        when(userService.findByUsername(any())).thenReturn(Optional.of(new User()));

        List<Topic> topics = new ArrayList<>();

        Anecdote a = Anecdote.builder()
                .author(User.builder().username("mathis").build())
                .content("test")
                .downvotes(0)
                .upvotes(0)
                .topics(topics)
                .build();
        AnecdoteDTO adto = new AnecdoteDTO(a);
        ArrayList<AnecdoteDTO> array = new ArrayList<>();
        array.add(adto);
        array.add(adto);
        array.add(adto);

        when(ratingService.getStarred(any())).thenReturn(AnecdotesResponse.builder()
                .anecdotes(array)
                .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/anecdote/starred/username")
        ).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes", hasSize(3)));
    }

}
