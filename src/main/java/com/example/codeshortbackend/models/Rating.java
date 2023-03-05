package com.example.codeshortbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating")
public class Rating {

    @EmbeddedId
    UserAnecdoteKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("anecdoteId")
    @JoinColumn(name = "anecdote_id")
    Anecdote anecdote;

    private Vote vote;

    private boolean starred;

    public Rating(User user, Anecdote anecdote, Vote vote, boolean starred) {
        this.id =  new UserAnecdoteKey(user.getId(), anecdote.getId());
        this.user = user;
        this.anecdote = anecdote;
        this.vote = vote;
        this.starred = starred;
    }
}
