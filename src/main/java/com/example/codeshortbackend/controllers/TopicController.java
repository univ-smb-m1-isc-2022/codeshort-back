package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.responses.TopicsResponse;
import com.example.codeshortbackend.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("")
    public ResponseEntity<TopicsResponse> all() {
        return ResponseEntity.ok(topicService.all());
    }
}
