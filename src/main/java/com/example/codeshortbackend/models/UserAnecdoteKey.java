package com.example.codeshortbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
class UserAnecdoteKey implements Serializable {

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "anecdote_id")
    Integer anecdoteId;
}
