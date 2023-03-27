package com.example.codeshortbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "anecdote")
public class Anecdote {

    @Id
    @GeneratedValue
    private Integer id;
    private String content;
    @ManyToOne
    @JoinColumn(name="author_id", nullable=false)
    private User author;
    @OneToMany(mappedBy = "anecdote")
    List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "topic_anecdote",
            joinColumns = @JoinColumn(name = "anecdote_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "topic_name", referencedColumnName="name")
    )
    private List<Topic> topics;

    @OneToMany(mappedBy = "anecdote")
    private List<AnecdoteReport> reports;

    private Integer upvotes;
    private Integer downvotes;

    public Anecdote(String content, User author, List<Topic> topics) {
        this.content = content;
        this.author = author;
        this.topics = topics;
        this.upvotes = 0;
        this.downvotes = 0;
    }

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
}
