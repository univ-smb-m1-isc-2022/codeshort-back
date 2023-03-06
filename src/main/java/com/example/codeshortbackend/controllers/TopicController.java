package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.responses.TopicDTO;
import com.example.codeshortbackend.responses.TopicsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepository topicRepository;

    @GetMapping("")
    public ResponseEntity<TopicsResponse> all() {
        List<Topic> topics = topicRepository.findAll();
        List<TopicDTO> resultTopic = new ArrayList<>();
        for (Topic t: topics) {
            resultTopic.add(new TopicDTO(t));
        }
        return ResponseEntity.ok(new TopicsResponse(resultTopic));
    }
}
