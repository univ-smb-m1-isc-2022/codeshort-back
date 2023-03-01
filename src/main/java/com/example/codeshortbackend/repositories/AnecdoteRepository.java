package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnecdoteRepository extends JpaRepository<Anecdote, Integer> {
    List<Anecdote> findAllByTopicsIn(List<Topic> topicList);
}