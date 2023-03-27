package com.example.codeshortbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rating_comment")
public class RatingComment {

    @EmbeddedId
    UserCommentKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    Comment comment;

    private Vote vote;

    public RatingComment(User user, Comment comment, Vote ratingVote) {
        this.id =  new UserCommentKey(user.getId(), comment.getId());
        this.comment = comment;
        this.user = user;
        this.vote = ratingVote;
    }
}
