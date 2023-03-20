package com.example.codeshortbackend.controllers;

import com.example.codeshortbackend.models.User;
import com.example.codeshortbackend.services.AuthenticationService;
import com.example.codeshortbackend.services.FileService;
import com.example.codeshortbackend.services.FollowerService;
import com.example.codeshortbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest {

    private AuthenticationService authenticationService;
    private UserService userService;
    private FileService fileService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        fileService = mock(FileService.class);
        authenticationService = mock(AuthenticationService.class);
        userService = mock(UserService.class);
        mockMvc = standaloneSetup(new UserController(authenticationService,userService, fileService)).build();
    }

    @Test
    public void changeProfilePicture_userShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.empty());

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void changeProfilePicture_fileShouldBadRequest() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(fileService.uploadProfilePicture(any(), any())).thenReturn(null);

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void changeProfilePicture_isOk() throws Exception {
        when(authenticationService.findUser()).thenReturn(Optional.of(new User()));
        when(fileService.uploadProfilePicture(any(), any())).thenReturn("filename.png");

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.png", "text/plain", "some xml".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/user/picture")
                .file(firstFile)
        ).andExpect(status().isOk());
    }
}
