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
@Table(name = "anecdote_report")
public class AnecdoteReport {
    @Id
    @GeneratedValue
    private Integer id;
    private String content;
    @ManyToOne
    private Anecdote anecdote;
}
