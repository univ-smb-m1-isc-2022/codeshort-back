package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Anecdote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnecdoteFromTopicsResponse {
    List<Anecdote> anecdotes;
}
