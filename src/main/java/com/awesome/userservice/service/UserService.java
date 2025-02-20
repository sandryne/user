package com.awesome.userservice.service;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.model.User;

import java.util.Optional;

public interface UserService {
    UserDto registerUser(UserDto userDto);

    Optional<UserDto> findByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    void changePassword(Long userId, PasswordChangeRequest passwordChangeRequest);
}

