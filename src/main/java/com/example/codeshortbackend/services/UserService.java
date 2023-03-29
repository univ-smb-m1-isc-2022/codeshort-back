package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.UserTopicsRequest;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public SuccessResponse changeProfilePicture(User user, String filename) {

        user.setPictureUri(filename);

        userRepository.save(user);

        return SuccessResponse.builder()
                .response(filename)
                .build();
    }

    public SuccessResponse addTopics(User user, List<Topic> topics) {

        user.setTopics(topics);
        userRepository.save(user);

        return SuccessResponse.builder()
                .response("topics added")
                .build();
    }

    public boolean existsByUsername( String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
