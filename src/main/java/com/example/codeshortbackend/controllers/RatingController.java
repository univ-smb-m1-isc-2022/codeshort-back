package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.repositories.UserRepository;
import com.example.codeshortbackend.requests.RatingRequest;
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
@RequestMapping("/api/anecdote")
@RequiredArgsConstructor
public class RatingController {

    private final AnecdoteRepository anecdoteRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    @PostMapping("/{anecdoteId}/rating")
    public ResponseEntity<?> rate(
            @PathVariable Integer anecdoteId,
            @RequestBody RatingRequest request
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Optional<Anecdote> anecdote = anecdoteRepository.findById(anecdoteId);

        if(anecdote.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Error, The corpus doesn't exist");
        }

        Optional<Rating> rating = ratingRepository.findByUserAndAnecdote(user, anecdote.get());

        System.out.println(request.isStarred());
        if(rating.isPresent()) {
            Vote lastRating = rating.get().getVote();
            Vote newRating = request.getVote();
            if(newRating != lastRating){
                if(newRating == Vote.UPVOTE) {
                    anecdote.get().changeUpvote(true);
                    if(lastRating == Vote.DOWNVOTE) anecdote.get().changeDownvote(false);
                }
                if(newRating == Vote.DOWNVOTE) {
                    anecdote.get().changeDownvote(true);
                    if(lastRating == Vote.UPVOTE) anecdote.get().changeUpvote(false);
                }
                if(newRating == Vote.NONE) {
                    if(lastRating == Vote.DOWNVOTE) anecdote.get().changeDownvote(false);
                    if(lastRating == Vote.UPVOTE) anecdote.get().changeUpvote(false);
                }
            }
            rating.get().setVote(request.getVote());
            rating.get().setStarred(request.isStarred());
            anecdoteRepository.save(anecdote.get());
            ratingRepository.save(rating.get());
            return ResponseEntity.ok(new SuccessResponse("Rating updated"));
        } else {
            Vote ratingVote = request.getVote();
            Rating newRating = new Rating(user, anecdote.get(), ratingVote, request.isStarred());
            ratingRepository.save(newRating);
            if(ratingVote == Vote.UPVOTE) anecdote.get().changeUpvote(true);
            if(ratingVote == Vote.DOWNVOTE) anecdote.get().changeDownvote(true);
            anecdoteRepository.save(anecdote.get());
            return ResponseEntity.ok(new SuccessResponse("Rating created"));
        }
    }

    @GetMapping("/starred")
    public ResponseEntity<?> getStarred() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Rating> ratings = ratingRepository.findAllByUserAndStarredTrue(user);
        List<AnecdoteDTO> anecdotes = new ArrayList<>();

        for (Rating r: ratings) {
            anecdotes.add(new AnecdoteDTO(r.getAnecdote()));
        }
        return ResponseEntity.ok(new AnecdotesResponse(anecdotes));
    }
}
