package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.requests.AuthenticationRequest;
import com.example.codeshortbackend.requests.RegisterRequest;
import com.example.codeshortbackend.responses.*;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
public class AuthenticationControllerTest {

    private AuthenticationService authenticationService;
    private UserService userService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        mockMvc = standaloneSetup(new AuthenticationController(authenticationService, userService)).build();
    }

    @Test
    public void register_shouldBadRequest() throws Exception {
        when(userService.existsByUsername(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register")
                .content(asJsonString(RegisterRequest.builder()
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void register_shouldReturnTokenAndProfileURI() throws Exception {
        when(userService.existsByUsername(anyString())).thenReturn(false);
        when(authenticationService.register(any())).thenReturn(
                AuthenticationResponse.builder()
                        .token("token")
                        .pictureUri("uri")
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register")
                .content(asJsonString(RegisterRequest.builder()
                        .build())
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pictureUri").value("uri"));
    }

    @Test
    public void authenticate_shouldBadRequest() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/authenticate")
                        .content(asJsonString(AuthenticationRequest.builder()
                                .username("user")
                                .password("password")
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void authenticate_shouldReturnTokenAndProfileURI() throws Exception {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(authenticationService.authenticate(any(), any())).thenReturn(
                AuthenticationResponse.builder()
                        .token("token")
                        .pictureUri("uri")
                        .build()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/authenticate")
                        .content(asJsonString(AuthenticationRequest.builder()
                                .username("user")
                                .password("password")
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pictureUri").value("uri"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
