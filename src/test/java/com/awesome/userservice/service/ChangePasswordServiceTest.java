package com.awesome.userservice.service;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.UserRepository;
import com.awesome.userservice.util.PasswordUtil;
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
class ChangePasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChangePasswordService changePasswordService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Paul");
        user.setEmail("paul@example.com");
        user.setPassword("plainTxt-password");

    }

    @Test
    void testChangePassword_WhenOldPasswordIsInvalid_ShouldReturnError() {
        String password="invalid";
        user.setPassword(PasswordUtil.hashPassword("plainTxt-password"));
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword(password);
        passwordChangeRequest.setNewPassword("new");

        // When & Then
        UserException exception = assertThrows(
                UserException.class,
                () -> changePasswordService.execute(user.getId(), passwordChangeRequest),
                "Expected registerUser to throw, but it didn't"
        );

        assertTrue(exception.getErrorCode().contains(UserExceptionErrorCode.USER_ERR_INVALID_PASSWORD));

        // Verify repository interaction
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testChangePassword_WhenOldPasswordIsValid_ShouldReturnNoError() {
        String password="plainTxt-password";
        user.setPassword(PasswordUtil.hashPassword(password));
        // Given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));


        // When
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword(password);
        passwordChangeRequest.setNewPassword("new");
        // When & Then

        assertDoesNotThrow(() -> changePasswordService.execute(user.getId(), passwordChangeRequest));

        // Verify repository interaction
        verify(userRepository, times(1)).findById(user.getId());
    }
}