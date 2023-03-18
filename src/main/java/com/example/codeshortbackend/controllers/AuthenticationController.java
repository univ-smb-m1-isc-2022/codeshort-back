package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.responses.AuthenticationResponse;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.requests.AuthenticationRequest;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
