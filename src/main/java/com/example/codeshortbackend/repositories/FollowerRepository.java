package com.example.codeshortbackend.repositories;

import com.example.codeshortbackend.models.Follower;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer>{
    List<Follower> findAllByFollower(User follower);
    boolean existsByUserAndFollower(User user, User follower);
}
