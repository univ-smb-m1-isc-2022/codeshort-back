package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnecdoteRepository extends JpaRepository<Anecdote, Integer> {
    List<Anecdote> findAllByTopicsIn(List<Topic> topicList);
    List<Anecdote> findTop20ByOrderByUpvotesDesc();
    List<Anecdote> findAllByAuthor(User author);
}