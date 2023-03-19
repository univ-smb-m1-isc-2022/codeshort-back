package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.requests.AuthenticationRequest;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.CommentService;
import com.example.codeshortbackend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
public class CommentControllerTest {

    private AuthenticationService authenticationService;
    private AnecdoteService anecdoteService;
    private CommentService commentService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        authenticationService = mock(AuthenticationService.class);
        anecdoteService = mock(AnecdoteService.class);
        commentService = mock(CommentService.class);
        mockMvc = standaloneSetup(new CommentController(anecdoteService, authenticationService, commentService)).build();
    }

    @Test
    public void createComment_userNotFound() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        mockMvc.perform(createCommentRequest())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createComment_anecdoteNotFound() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(createCommentRequest())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createComment_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(anecdoteService.findById(any())).thenReturn(Optional.of(new Anecdote()));
        when(commentService.createComment(any(), any(), any())).thenReturn(SuccessResponse.builder().build());

        mockMvc.perform(createCommentRequest())
                .andExpect(status().isOk());
    }

    @Test
    public void all_anecdoteNotFound() throws Exception {
        when(anecdoteService.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/anecdote/0/comment/all")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void all_shouldGiveThree() throws Exception {
        Comment c = Comment.builder()
                .user(User.builder().username("mathis").build())
                .comment("test")
                .build();
        CommentDTO cdto = new CommentDTO(c);
        ArrayList<CommentDTO> array = new ArrayList<>();
        array.add(cdto);
        array.add(cdto);
        array.add(cdto);

        when(anecdoteService.findById(any())).thenReturn(Optional.of(new Anecdote()));
        when(commentService.all(any())).thenReturn(CommentsResponse.builder()
                .comments(array)
                .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/anecdote/0/comment/all")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments", hasSize(3)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MockHttpServletRequestBuilder createCommentRequest() {
        return MockMvcRequestBuilders
                .post("/api/anecdote/0/comment")
                .content(asJsonString(CreateCommentRequest.builder()
                        .content("test")
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}
