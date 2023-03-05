package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
