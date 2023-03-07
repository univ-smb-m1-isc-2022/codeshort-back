package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.Vote;
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
    String pictureUri;
    Integer upvotes;
    Integer downvotes;
    List<String> topics = new ArrayList<>();
    Vote vote;
    boolean starred;

    public AnecdoteDTO(Anecdote anecdote) {
        this.id = anecdote.getId();
        this.content = anecdote.getContent();
        this.author = anecdote.getAuthor().getUsername();
        this.pictureUri = anecdote.getAuthor().getPictureUri();
        this.upvotes = anecdote.getUpvotes();
        this.downvotes = anecdote.getDownvotes();
        for (Topic topic: anecdote.getTopics()) {
            topics.add(topic.getName());
        }
        this.vote = Vote.NONE;
        this.starred = false;
    }

    public AnecdoteDTO(Rating rating) {
        this.id = rating.getAnecdote().getId();
        this.content = rating.getAnecdote().getContent();
        this.author = rating.getAnecdote().getAuthor().getUsername();
        this.pictureUri = rating.getAnecdote().getAuthor().getPictureUri();
        this.upvotes = rating.getAnecdote().getUpvotes();
        this.downvotes = rating.getAnecdote().getDownvotes();
        for (Topic topic: rating.getAnecdote().getTopics()) {
            topics.add(topic.getName());
        }
        this.vote = rating.getVote();
        this.starred = rating.isStarred();
    }
}
