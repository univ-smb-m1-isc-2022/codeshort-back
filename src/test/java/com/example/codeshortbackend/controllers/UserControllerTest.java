package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.models.Vote;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.requests.UserTopicsRequest;
import com.example.codeshortbackend.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest {

    private AuthenticationService authenticationService;
    private UserService userService;
    private FileService fileService;
    private TopicService topicService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        fileService = mock(FileService.class);
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        topicService = mock(TopicService.class);
        mockMvc = standaloneSetup(new UserController(authenticationService,userService, fileService, topicService)).build();
    }

    @Test
    public void changeProfilePicture_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void changeProfilePicture_fileShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(fileService.uploadProfilePicture(any(), any())).thenReturn(null);

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void changeProfilePicture_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(fileService.uploadProfilePicture(any(), any())).thenReturn("filename.png");

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isOk());
    }

    @Test
    public void addTopics_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/topics")
                .content(asJsonString(UserTopicsRequest.builder()
                        .topics(new ArrayList<>())
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addTopics_fileShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(topicService.findByName(any())).thenReturn(Optional.empty());

        List<String> topics = new ArrayList<>();
        topics.add("test");
        topics.add("test2");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/topics")
                .content(asJsonString(UserTopicsRequest.builder()
                        .topics(topics)
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void addTopics_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(topicService.findByName(any())).thenReturn(Optional.of(new Topic()));

        List<String> topics = new ArrayList<>();
        topics.add("test");
        topics.add("test2");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/topics")
                .content(asJsonString(UserTopicsRequest.builder()
                        .topics(topics)
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
