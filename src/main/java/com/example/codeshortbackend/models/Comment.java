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
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    User user;

    @ManyToOne
    @JoinColumn(name = "anecdote_id", nullable=false)
    Anecdote anecdote;

    private String comment;

    private Integer upvotes;
    private Integer downvotes;

    public void changeUpvote(boolean isIncremented){
        if (isIncremented) {
            upvotes++;
        }
        else {
            upvotes--;
        }
    }

    public void changeDownvote(boolean isIncremented){
        if (isIncremented) {
            downvotes++;
        }
        else {
            downvotes--;
        }
    }

    public Comment(User user, Anecdote anecdote, String comment) {
        this.user = user;
        this.anecdote = anecdote;
        this.comment = comment;
        this.upvotes = 0;
        this.downvotes = 0;
    }
}