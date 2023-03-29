package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Topic;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.TopicRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.requests.UserTopicsRequest;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.FileService;
import com.example.codeshortbackend.services.TopicService;
import com.example.codeshortbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final FileService fileService;
    private final TopicService topicService;


    @PostMapping("/picture")
    public ResponseEntity<?> changeProfilePicture(@RequestParam("file") MultipartFile file)
    {
        Optional<User> user = authenticationService.findUser();

        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        // TODO gérer si le file est en .png .jpg
        String fileName = fileService.uploadProfilePicture(file, user.get().getUsername());
        if(fileName == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, unable to copy you file, server accept only png/jpg");
        }

        return ResponseEntity.ok(this.userService.changeProfilePicture(user.get(), fileName));
    }

    @PostMapping("/topics")
    public ResponseEntity<?> addTopics(@RequestBody UserTopicsRequest request) {
        Optional<User> user = authenticationService.findUser();

        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        List<Topic> topics = new ArrayList<>();

        for (String topicName : request.getTopics()) {
            Optional<Topic> topic = topicService.findByName(topicName);

            if(topic.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Topic not found");
            }

            topics.add(topic.get());
        }

        return ResponseEntity.ok(this.userService.addTopics(user.get(), topics));
    }
}
