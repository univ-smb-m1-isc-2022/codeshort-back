package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.requests.AuthenticationRequest;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.responses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
            ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
