package com.example.codeshortbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topic")
public class Topic {
    @Id
    private String name;

    @ManyToMany
    @JoinTable(
            name = "topic_anecdote",
            joinColumns = @JoinColumn(name = "topic_name"),
            inverseJoinColumns = @JoinColumn(name = "anecdote_id"))
    List<Anecdote> anecdotes;
}
