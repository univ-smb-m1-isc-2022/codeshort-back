package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.services.FileService;
import com.example.codeshortbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final FileService fileService;


    @PostMapping("/picture")
    public ResponseEntity<?> changeProfilePicture(@RequestParam("file") MultipartFile file)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("User not found");
        }

        // TODO gérer si le file est en .png .jpg
        String fileName = fileService.uploadFile(file);

        if(fileName == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, unable to copy you file");
        }

        return ResponseEntity.ok(this.userService.changeProfilePicture(user.get(), fileName));
    }
}
