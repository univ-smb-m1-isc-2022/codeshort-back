package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.FollowerRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.FollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @GetMapping("")
    public ResponseEntity<?> getUsersFollowed() {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> follower = userRepository.findByUsername(usernameFollower);
        if(follower.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        return ResponseEntity.ok(followerService.getUsersFollowed(follower.get()));
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<?> followUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> follower = userRepository.findByUsername(usernameFollower);
        if(follower.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        Optional<User> userFollowed = userRepository.findByUsername(username);
        if(userFollowed.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        if(followerRepository.existsByUserAndFollower(userFollowed.get(), follower.get())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user is already followed");
        }

        return ResponseEntity.ok(followerService.followUser(userFollowed.get(), follower.get()));
    }

    @PostMapping("/unfollow/{username}")
    public ResponseEntity<?> unfollowUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> follower = userRepository.findByUsername(usernameFollower);
        if(follower.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        Optional<User> userFollowed = userRepository.findByUsername(username);
        if(userFollowed.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        Optional<Follower> follow = followerRepository.findByUserAndFollower(userFollowed.get(), follower.get());
        if(follow.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The relation doesn't exists");
        }

        return ResponseEntity.ok(followerService.unfollowUser(follow.get()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> follower = userRepository.findByUsername(usernameFollower);
        if(follower.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        Optional<User> userFollowed = userRepository.findByUsername(username);
        if(userFollowed.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User followed found");
        }

        return ResponseEntity.ok(followerService.getUser(userFollowed.get(),follower.get()));
    }
}
