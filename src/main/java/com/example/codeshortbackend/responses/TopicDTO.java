package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {
    String name;

    public TopicDTO(Topic topic) {
        this.name = topic.getName();
    }
}
