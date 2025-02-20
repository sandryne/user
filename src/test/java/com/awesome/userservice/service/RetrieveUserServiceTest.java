package com.awesome.userservice.service;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.UserRepository;
import com.awesome.userservice.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetrieveUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RetrieveUserService retrieveUserService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Paul");
        user.setEmail("paul@example.com");
        user.setPassword("plainTxt-password");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Paul");
        userDto.setEmail("paul@example.com");
        userDto.setPassword("plainTxt-password");
    }


    @Test
    void testFindByEmail_WhenUserExists_ShouldReturnUser() {
        String email="paul@example.com";
        // Given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        // When
        Optional<UserDto> foundUser = retrieveUserService.findByEmail(user.getEmail());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(email);



    }

    @Test
    void testFindByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> foundUser = retrieveUserService.findByEmail("unknown@example.com");

        // Then
        assertFalse(foundUser.isPresent());

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail("unknown@example.com");
    }


}

