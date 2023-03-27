package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.Anecdote;
import com.example.codeshortbackend.models.Follower;
import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.requests.AnecdoteFromTopicsRequest;
import com.example.codeshortbackend.requests.CreateAnecdoteRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.AnecdoteService;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.FollowerService;
import com.example.codeshortbackend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class FollowerControllerTest {
    private AuthenticationService authenticationService;
    private FollowerService followerService;
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        followerService = mock(FollowerService.class);
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        mockMvc = standaloneSetup(new FollowerController(followerService,userService, authenticationService)).build();
    }

    @Test
    public void getUsersFollowed_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getUsersFollowed_shouldGiveThree() throws Exception {

        User user = User.builder().username("username").build();
        UserDTO userDTO = new UserDTO(user);
        ArrayList<UserDTO> users = new ArrayList<>();
        users.add(userDTO);
        users.add(userDTO);
        users.add(userDTO);

        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(followerService.getUsersFollowed(any())).thenReturn(UserFollowedResponse.builder()
                .usersFollowed(users)
                .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.usersFollowed").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.usersFollowed").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.usersFollowed", hasSize(3)));
    }

    @Test
    public void followUser_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/follow/username")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void followUser_userFollowedShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/follow/username")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void followUser_alreadyFollowedShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(followerService.existsByUserAndFollower(any(), any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/follow/username")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void followUser_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(followerService.existsByUserAndFollower(any(), any())).thenReturn(false);
        when(followerService.followUser(any(), any())).thenReturn(SuccessResponse.builder().build());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user/follow/username")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void unfollowUser_userNotFoundBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/unfollow/username")
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void unfollowUser_userFollowedNotFoundBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/unfollow/username")
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void unfollowUser_userNotFollowedBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(followerService.findByUserAndFollower(any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/unfollow/username")
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void unfollowUser_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(followerService.findByUserAndFollower(any(), any())).thenReturn(Optional.of(new Follower()));
        when(followerService.unfollowUser(any())).thenReturn(SuccessResponse.builder().build());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user/unfollow/username")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getUser_userNotFoundBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/username")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUser_userFollowedNotFoundBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/username")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUser_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(followerService.getUser(any(), any())).thenReturn(UserResponse.builder()
                .profilePictureURI("uriPicture")
                .isFollowed(true)
                .gitURI("uriGit")
                .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/username")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.profilePictureURI").value("uriPicture"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.followed").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gitURI").value("uriGit"));
    }

}
