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
    private List<Topic> topics;

    public Anecdote(String content, User author) {
        this.content = content;
        this.author = author;
    }
}
