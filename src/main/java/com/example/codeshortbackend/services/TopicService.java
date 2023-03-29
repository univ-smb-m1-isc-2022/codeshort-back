package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.responses.TopicDTO;
import com.example.codeshortbackend.responses.TopicsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicsResponse all() {
        List<Topic> topics = topicRepository.findAll();
        List<TopicDTO> resultTopic = new ArrayList<>();
        for (Topic t: topics) {
            resultTopic.add(new TopicDTO(t));
        }

        return TopicsResponse.builder()
                .topics(resultTopic)
                .build();
    }

    public Optional<Topic> findByName(String name) {
        return topicRepository.findByName(name);
    }
}
