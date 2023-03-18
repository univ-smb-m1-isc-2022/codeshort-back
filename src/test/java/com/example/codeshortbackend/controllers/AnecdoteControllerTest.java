package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import com.example.codeshortbackend.responses.UserAnecdoteResponse;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class AnecdoteControllerTest {
    private AuthenticationService authenticationService;
    private AnecdoteService anecdoteService;
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        anecdoteService = mock(AnecdoteService.class);
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        mockMvc = standaloneSetup(new AnecdoteController(anecdoteService,authenticationService, userService)).build();
    }

    @Test
    public void findUser_shouldBadRequest() throws Exception {
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
    public void createAnecdote_isOk() throws Exception {
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

    @Test
    public void allRandom_shouldGiveThree() throws Exception{
        List<AnecdoteDTO> list = createAnecdotesDTOList(3);

        when(anecdoteService.allRandom()).thenReturn(
                AnecdotesResponse.builder().anecdotes(list).build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/anecdote/random"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes", hasSize(3)));

    }

    @Test
    public void allFromTopics_shouldGiveThree() throws Exception{
        List<AnecdoteDTO> list = createAnecdotesDTOList(3);

        when(anecdoteService.allFromTopic(any())).thenReturn(
                AnecdotesResponse.builder().anecdotes(list).build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/anecdote/topics")
                        .content(asJsonString(AnecdoteFromTopicsRequest.builder()
                                .topics(new ArrayList<>())
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes", hasSize(3)));

    }

    @Test
    public void allFromUser_shouldGiveThree() throws Exception{
        List<AnecdoteDTO> list = createAnecdotesDTOList(3);

        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(anecdoteService.allFromUser(any())).thenReturn(
                UserAnecdoteResponse.builder().anecdotes(list).build()
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/anecdote/user/test"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.anecdotes", hasSize(3)));

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Anecdote createAnecdote() {
        Anecdote a = Anecdote.builder().author(User.builder()
                .username("test")
                .build()).build();
        a.setTopics(new ArrayList<>());
        return a;
    }

    public List<AnecdoteDTO> createAnecdotesDTOList ( Integer nb ) {
        Anecdote anecdote = createAnecdote();
        List<AnecdoteDTO> list = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            list.add(new AnecdoteDTO(anecdote));
        }
        return list;
    }
}
