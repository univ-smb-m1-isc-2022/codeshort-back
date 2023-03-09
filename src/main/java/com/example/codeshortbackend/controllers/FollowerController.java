package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.FollowerRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.responses.*;
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
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<UserFollowedResponse> getUsersFollowed() {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();
        User follower = userRepository.findByUsername(usernameFollower).orElseThrow();
        List<Follower> usersFollowed = followerRepository.findAllByFollower(follower);

        List<UserDTO> users = new ArrayList<>();

        for (Follower f: usersFollowed) {
            users.add(new UserDTO(f));
        }

        return ResponseEntity.ok(new UserFollowedResponse(users));
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<?> followUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();
        User follower = userRepository.findByUsername(usernameFollower).orElseThrow();
        User userFollowed = userRepository.findByUsername(username).orElseThrow();

        if(followerRepository.existsByUserAndFollower(userFollowed, follower)) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user is already followed");
        }

        Follower newFollower = new Follower(userFollowed, follower);
        followerRepository.save(newFollower);

        return ResponseEntity.ok(new SuccessResponse("User follow"));
    }

    @PostMapping("/unfollow/{username}")
    public ResponseEntity<?> unfollowUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();
        User follower = userRepository.findByUsername(usernameFollower).orElseThrow();
        User userFollowed = userRepository.findByUsername(username).orElseThrow();

        Optional<Follower> follow = followerRepository.findByUserAndFollower(userFollowed, follower);
        if(follow.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The relation doesn't exists");
        }

        followerRepository.delete(follow.get());

        return ResponseEntity.ok(new SuccessResponse("User unfollow"));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String  username
    ) {
        String usernameFollower = SecurityContextHolder.getContext().getAuthentication().getName();
        User follower = userRepository.findByUsername(usernameFollower).orElseThrow();
        User userFollowed = userRepository.findByUsername(username).orElseThrow();

        boolean isFollowed = followerRepository.existsByUserAndFollower(userFollowed, follower);
        return ResponseEntity.ok(new UserResponse(follower.getPictureUri(), isFollowed, follower.getGithubUri()));
    }
}
