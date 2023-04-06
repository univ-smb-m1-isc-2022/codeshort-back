package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Follower;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.FollowerRepository;
import com.example.codeshortbackend.responses.SuccessResponse;
import com.example.codeshortbackend.responses.UserDTO;
import com.example.codeshortbackend.responses.UserFollowedResponse;
import com.example.codeshortbackend.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;

    public UserFollowedResponse getUsersFollowed(User follower) {
        List<Follower> usersFollowed = followerRepository.findAllByFollower(follower);

        List<UserDTO> users = new ArrayList<>();

        for (Follower f: usersFollowed) {
            users.add(new UserDTO(f));
        }

        return UserFollowedResponse.builder()
                .usersFollowed(users)
                .build();
    }

    public SuccessResponse followUser (User userFollowed, User follower) {
        Follower newFollower = new Follower(userFollowed, follower);
        followerRepository.save(newFollower);

        return SuccessResponse.builder()
                .response("User followed")
                .build();
    }

    public SuccessResponse unfollowUser( Follower follow) {
        followerRepository.delete(follow);
        return SuccessResponse.builder()
                .response("User unfollowed")
                .build();
    }

    public UserResponse getUser(User userFollowed, User follower) {
        boolean isFollowed = followerRepository.existsByUserAndFollower(userFollowed, follower);
        return UserResponse.builder()
                .profilePictureURI(userFollowed.getPictureUri())
                .isFollowed(isFollowed)
                .gitURI(userFollowed.getGithubUri())
                .build();
    }

    public boolean existsByUserAndFollower(User userFollowed, User follower) {
        return followerRepository.existsByUserAndFollower(userFollowed, follower);
    }

    public Optional<Follower> findByUserAndFollower(User userFollowed, User follower) {
        return followerRepository.findByUserAndFollower(userFollowed, follower);
    }


}
