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
public class RatingRequest {
    Vote vote;
    boolean starred;
}
