package com.awesome.userservice.service;

import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.Role;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.RoleRepository;
import com.awesome.userservice.repository.UserRepository;
import com.awesome.userservice.util.PasswordUtil;
import com.awesome.userservice.util.UserMapper;
import com.awesome.userservice.util.UserRoleEnum;
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
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private
    RoleRepository roleRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private EmailService emailService;


    @InjectMocks
    private RegisterService registerService;

    private User user;
    private UserDto userDto;
    private Role role;

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

        role = new Role();
        role.setId(1L);
        role.setName(UserRoleEnum.ROLE_USER.toString());
    }

    @Test
    void testRegisterUser_ShouldSaveUserWithHashedPassword() {
        String plainTextPassword = "password";
        // Given
        when(roleRepository.findByName(UserRoleEnum.ROLE_USER.toString())).thenReturn(Optional.of(role));
        when(userMapper.toUser(any(UserDto.class))).thenReturn(user);
        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setPassword(PasswordUtil.hashPassword(plainTextPassword)); // Modify password before returning
            return savedUser;
        }).when(userRepository).save(any(User.class));
        doAnswer(invocation -> {
            UserDto mappedUser = new UserDto();
            mappedUser.setPassword(PasswordUtil.hashPassword(plainTextPassword)); // Modify password before returning
            return mappedUser;
        }).when(userMapper).toUserDto(any(User.class));

        // When
        UserDto savedUser = registerService.execute(userDto);

        // Then
        assertNotNull(savedUser);
        assertTrue(PasswordUtil.checkPassword(plainTextPassword, savedUser.getPassword()));
        assertFalse(savedUser.isVerified());

        // Verify repository interaction
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_WhenUserExists_ShouldReturnError() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When & Then
        UserException exception = assertThrows(
                UserException.class,
                () -> registerService.execute(userDto),
                "Expected registerUser to throw, but it didn't"
        );

        assertTrue(exception.getErrorCode().contains(UserExceptionErrorCode.USER_ERR_DUPLICATED_EMAIL));
    }
}