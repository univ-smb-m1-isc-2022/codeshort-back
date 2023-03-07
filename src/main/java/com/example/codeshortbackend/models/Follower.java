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
@Table(name = "follow")
public class Follower {

    @EmbeddedId
    FollowerUserKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    User follower;

    public Follower(User userFollowed, User follower) {
        this.id =  new FollowerUserKey(userFollowed.getId(), follower.getId());
        this.user = userFollowed;
        this.follower = follower;
    }
}
