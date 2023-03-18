package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnecdoteControllerTest {
    private AuthenticationService authenticationService;
    private AnecdoteService anecdoteService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        anecdoteService = mock(AnecdoteService.class);
        authenticationService = mock(AuthenticationService.class);
        mockMvc = standaloneSetup(new AnecdoteController(anecdoteService,authenticationService)).build();
    }

    @Test
    public void createAnecdote_badRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/anecdote")
                .content(asJsonString(CreateAnecdoteRequest.builder()
                        .content("test")
                        .topics(new ArrayList<>())
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void createAnecdote_success() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteService.createAnecdote(any(),any())).thenReturn(SuccessResponse.builder().build());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/anecdote")
                .content(asJsonString(CreateAnecdoteRequest.builder()
                        .content("test")
                        .topics(new ArrayList<>())
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
