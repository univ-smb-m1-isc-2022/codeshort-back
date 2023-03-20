package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.responses.TopicDTO;
import com.example.codeshortbackend.responses.TopicsResponse;
import com.example.codeshortbackend.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class TopicControllerTest {
    private TopicService topicService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        topicService = mock(TopicService.class);
        mockMvc = standaloneSetup(new TopicController(topicService)).build();
    }

    @Test
    public void all_shouldGiveThree() throws Exception {
        Topic t = Topic.builder()
                .name("test")
                .anecdotes(new ArrayList<>())
                .build();

        TopicDTO tdto = new TopicDTO(t);
        ArrayList<TopicDTO> array = new ArrayList<>();
        array.add(tdto);
        array.add(tdto);
        array.add(tdto);

        when(topicService.all()).thenReturn(TopicsResponse.builder()
                .topics(array)
                .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/topic")
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.topics", hasSize(3)));
    }

}
