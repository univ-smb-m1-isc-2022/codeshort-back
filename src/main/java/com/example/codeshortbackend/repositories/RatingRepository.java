package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByUserAndAnecdote(User user, Anecdote anecdote);
    List<Rating> findAllByUserAndStarredTrue(User user);
    Optional<Rating> findByAnecdoteAndUser(Anecdote anecdote, User user);
}
