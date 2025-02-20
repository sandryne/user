package com.awesome.userservice.controller;

import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.model.User;
import com.awesome.userservice.service.UserService;
import com.awesome.userservice.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;  // Use the interface instead of UserServiceImpl

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UserDto user;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        user = new UserDto();
        user.setId(1L);
        user.setName("Paul");
        user.setEmail("paul@example.com");
        user.setPassword("password12345");
    }

    @Test
    void testRegisterUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        when(userService.registerUser(any(UserDto.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()) // Fix: Should return 201 Created
                .andExpect(jsonPath("$.name").value("Paul"))
                .andExpect(jsonPath("$.email").value("paul@example.com"));

        // Verify service call
        verify(userService, times(1)).registerUser(any(UserDto.class));
    }

    @Test
    void testGetUserByEmail_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        when(userService.findByEmail("paul@example.com")).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/paul@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)) // Fix: Added headers
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("paul@example.com"));

        // Verify service call
        verify(userService, times(1)).findByEmail("paul@example.com");
    }

    @Test
    void testGetUserByEmail_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(userService.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/unknown@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)) // Fix: Added headers
                .andExpect(status().isNotFound());

        // Verify service call
        verify(userService, times(1)).findByEmail("unknown@example.com");
    }
}
