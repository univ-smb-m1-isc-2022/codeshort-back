package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Rating;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.models.Vote;
import com.example.codeshortbackend.repositories.AnecdoteRepository;
import com.example.codeshortbackend.repositories.RatingRepository;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.AnecdotesResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    private final AnecdoteRepository anecdoteRepository;

    public SuccessResponse rate(Anecdote anecdote, User user, RatingRequest request) {

        Optional<Rating> rating = ratingRepository.findByUserAndAnecdote(user, anecdote);

        if(rating.isPresent()) {
            Vote lastRating = rating.get().getVote();
            Vote newRating = request.getVote();
            if(newRating != lastRating){
                if(newRating == Vote.UPVOTE) {
                    anecdote.changeUpvote(true);
                    if(lastRating == Vote.DOWNVOTE) anecdote.changeDownvote(false);
                }
                if(newRating == Vote.DOWNVOTE) {
                    anecdote.changeDownvote(true);
                    if(lastRating == Vote.UPVOTE) anecdote.changeUpvote(false);
                }
                if(newRating == Vote.NONE) {
                    if(lastRating == Vote.DOWNVOTE) anecdote.changeDownvote(false);
                    if(lastRating == Vote.UPVOTE) anecdote.changeUpvote(false);
                }
            }
            rating.get().setVote(request.getVote());
            rating.get().setStarred(request.isStarred());
            anecdoteRepository.save(anecdote);
            ratingRepository.save(rating.get());
            return SuccessResponse.builder()
                    .response("Rating updated")
                    .build();
        } else {
            Vote ratingVote = request.getVote();
            Rating newRating = new Rating(user, anecdote, ratingVote, request.isStarred());
            ratingRepository.save(newRating);
            if(ratingVote == Vote.UPVOTE) anecdote.changeUpvote(true);
            if(ratingVote == Vote.DOWNVOTE) anecdote.changeDownvote(true);
            anecdoteRepository.save(anecdote);
            return SuccessResponse.builder()
                    .response("Rating created")
                    .build();
        }
    }

    public AnecdotesResponse getStarred(User user) {
        List<Rating> ratings = ratingRepository.findAllByUserAndStarredTrue(user);
        List<AnecdoteDTO> anecdotes = new ArrayList<>();

        for (Rating r: ratings) {
            anecdotes.add(new AnecdoteDTO(r));
        }

        return AnecdotesResponse.builder()
                .anecdotes(anecdotes)
                .build();
    }
}
