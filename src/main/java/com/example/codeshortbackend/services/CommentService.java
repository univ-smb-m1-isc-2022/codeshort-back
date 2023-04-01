package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.*;
import com.example.codeshortbackend.repositories.CommentRepository;
import com.example.codeshortbackend.repositories.RatingCommentRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.requests.RatingCommentRequest;
import com.example.codeshortbackend.requests.RatingRequest;
import com.example.codeshortbackend.responses.AnecdoteDTO;
import com.example.codeshortbackend.responses.CommentDTO;
import com.example.codeshortbackend.responses.CommentsResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final RatingCommentRepository ratingCommentRepository;
    private final AuthenticationService authenticationService;

    public SuccessResponse createComment(User user, Anecdote anecdote, CreateCommentRequest request) {

        Comment comment = new Comment(user, anecdote, request.getContent());
        commentRepository.save(comment);

        return SuccessResponse.builder()
                .response("Comment created")
                .build();
    }


    public CommentsResponse all(Anecdote anecdote) {

        Optional<User> user = authenticationService.findUser();

        List<CommentDTO> comments = new ArrayList<>();

        for (Comment c: anecdote.getComments()) {
            if(user.isEmpty()) comments.add(new CommentDTO(c));
            else {
                Optional<RatingComment> ratingComment = ratingCommentRepository.findByUserAndComment(user.get(), c);
                if(ratingComment.isEmpty()) comments.add(new CommentDTO(c));
                else comments.add(new CommentDTO(ratingComment.get()));
            }
        }

        return CommentsResponse.builder()
                .comments(comments)
                .build();
    }

    public SuccessResponse rate(Comment comment, User user, RatingCommentRequest request) {

        Optional<RatingComment> ratingComment = ratingCommentRepository.findByUserAndComment(user, comment);

        if(ratingComment.isPresent()) {
            Vote lastRating = ratingComment.get().getVote();
            Vote newRating = request.getVote();
            if(newRating != lastRating){
                if(newRating == Vote.UPVOTE) {
                    comment.changeUpvote(true);
                    if(lastRating == Vote.DOWNVOTE) comment.changeDownvote(false);
                }
                if(newRating == Vote.DOWNVOTE) {
                    comment.changeDownvote(true);
                    if(lastRating == Vote.UPVOTE) comment.changeUpvote(false);
                }
                if(newRating == Vote.NONE) {
                    if(lastRating == Vote.DOWNVOTE) comment.changeDownvote(false);
                    if(lastRating == Vote.UPVOTE) comment.changeUpvote(false);
                }
            }
            ratingComment.get().setVote(request.getVote());
            commentRepository.save(comment);
            ratingCommentRepository.save(ratingComment.get());
            return SuccessResponse.builder()
                    .response("Rating updated")
                    .build();
        } else {
            Vote ratingVote = request.getVote();
            RatingComment newRating = new RatingComment(user, comment, ratingVote);
            ratingCommentRepository.save(newRating);
            if(ratingVote == Vote.UPVOTE) comment.changeUpvote(true);
            if(ratingVote == Vote.DOWNVOTE) comment.changeDownvote(true);
            commentRepository.save(comment);
            return SuccessResponse.builder()
                    .response("Rating created")
                    .build();
        }
    }

    public Optional<Comment> findById(Integer commentId) {
        return commentRepository.findById(commentId);
    }
}
