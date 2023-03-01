package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findAllByNameIn(List<String> names);
}
