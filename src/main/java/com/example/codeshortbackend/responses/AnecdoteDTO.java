package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnecdoteDTO {

    Integer id;
    String content;
    String author;
    Integer upvotes;
    Integer downvotes;
    List<String> topics = new ArrayList<>();
    // TODO get les réactions qu'on a mises

    public AnecdoteDTO(Anecdote anecdote) {
        this.id = anecdote.getId();
        this.content = anecdote.getContent();
        this.author = anecdote.getAuthor().getUsername();
        this.upvotes = anecdote.getUpvotes();
        this.downvotes = anecdote.getDownvotes();
        for (Topic topic: anecdote.getTopics()) {
            topics.add(topic.getName());
        }
    }
}
