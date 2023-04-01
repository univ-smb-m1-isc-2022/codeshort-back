package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Comment;
import com.example.codeshortbackend.models.RatingComment;
import com.example.codeshortbackend.models.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    Integer id;
    String content;
    String author;
    Integer upvotes;
    Integer downvotes;
    Vote vote;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getUser().getUsername();
        this.content = comment.getComment();
        this.upvotes = comment.getUpvotes();
        this.downvotes = comment.getDownvotes();
        this.vote = Vote.NONE;
    }

    public CommentDTO(RatingComment ratingComment) {
        this.id = ratingComment.getComment().getId();
        this.author = ratingComment.getComment().getUser().getUsername();
        this.content = ratingComment.getComment().getComment();
        this.upvotes = ratingComment.getComment().getUpvotes();
        this.downvotes = ratingComment.getComment().getDownvotes();
        this.vote = ratingComment.getVote();
    }
}
