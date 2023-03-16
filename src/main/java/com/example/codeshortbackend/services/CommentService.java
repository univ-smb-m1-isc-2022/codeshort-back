package com.example.codeshortbackend.services;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.repositories.CommentRepository;
import com.example.codeshortbackend.requests.CreateCommentRequest;
import com.example.codeshortbackend.responses.CommentDTO;
import com.example.codeshortbackend.responses.CommentsResponse;
import com.example.codeshortbackend.responses.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public SuccessResponse createComment(User user, Anecdote anecdote, CreateCommentRequest request) {

        Comment comment = new Comment(user, anecdote, request.getContent());
        commentRepository.save(comment);

        return SuccessResponse.builder()
                .response("Comment created")
                .build();
    }


    public CommentsResponse all(Anecdote anecdote) {

        List<CommentDTO> comments = new ArrayList<>();

        for (Comment c: anecdote.getComments()) {
            comments.add(new CommentDTO(c));
        }

        return CommentsResponse.builder()
                .comments(comments)
                .build();
    }
}
