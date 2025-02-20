package com.awesome.userservice.service;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UpdateUserService updateUserService;

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
    void testUpdateProfile_WhenUserChangeName_ShouldReturnTheNewName() {
        // Given
        String newName="John Paul";
        userDto.setName(newName);
        when(userRepository.findByEmail("paul@example.com")).thenReturn(Optional.of(user));

        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setName(newName); // Modify name before returning
            return savedUser;
        }).when(userRepository).save(any(User.class));

        doAnswer(invocation -> {
            User userToMap = invocation.getArgument(0);
            UserDto mappedUser= new UserDto();
            mappedUser.setName(newName); // Modify name before returning
            mappedUser.setEmail(user.getEmail());
            return mappedUser;
        }).when(userMapper).toUserDto(any(User.class));


        // When
        UserDto updatedUser = updateUserService.execute(userDto);

        // Then
        assertNotNull(updatedUser);
        assertEquals(newName, updatedUser.getName());

        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateProfile_WhenUserNotExist_ShouldReturnError() {
        // Given
        userDto.setEmail("unknown@example.com");
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        // When & Then
        UserException exception = assertThrows(
                UserException.class,
                () -> updateUserService.execute(userDto),
                "Expected registerUser to throw, but it didn't"
        );

        assertTrue(exception.getErrorCode().contains(UserExceptionErrorCode.USER_ERR_USER_NOT_FOUND));
        // Verify repository interaction
        verify(userRepository, times(1)).findByEmail(userDto.getEmail());

    }


}

