package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    String content;
    String author;

    public CommentDTO(Comment comment) {
        this.author = comment.getUser().getUsername();
        this.content = comment.getComment();
    }
}
