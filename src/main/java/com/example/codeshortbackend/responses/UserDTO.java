package com.example.codeshortbackend.responses;

import com.example.codeshortbackend.models.Follower;
import com.example.codeshortbackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String user;
    String pictureUri;
    String githubUri;

    public UserDTO(Follower follower) {
        this.user = follower.getUser().getUsername();
        this.pictureUri = follower.getUser().getPictureUri();
        this.githubUri = follower.getUser().getGithubUri();
    }
    public UserDTO(User user) {
        this.user = user.getUsername();
        this.pictureUri = user.getPictureUri();
        this.githubUri = user.getGithubUri();
    }
}
