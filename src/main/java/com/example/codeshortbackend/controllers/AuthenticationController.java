package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.responses.AuthenticationResponse;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.requests.AuthenticationRequest;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
            ) {
        if(userService.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username already taken");
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
            ) {
        Optional<User> user = userService.findByUsername(request.getUsername());
        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The user doesn't exist");
        }
        return ResponseEntity.ok(authenticationService.authenticate(request, user.get()));
    }

}
