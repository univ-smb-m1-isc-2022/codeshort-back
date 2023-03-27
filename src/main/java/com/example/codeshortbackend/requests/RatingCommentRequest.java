package com.example.codeshortbackend.requests;

import com.example.codeshortbackend.models.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingCommentRequest {
    Vote vote;
}
