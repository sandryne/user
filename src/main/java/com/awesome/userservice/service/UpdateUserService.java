package com.awesome.userservice.service;

import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.util.UserMapper;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UpdateUserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDto execute(UserDto userDto) {
        User persistedUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserException(UserExceptionErrorCode.USER_ERR_USER_NOT_FOUND));

        if (!persistedUser.getName().equals(userDto.getName())) {
            persistedUser.setName(userDto.getName());
            User updatedUser = userRepository.save(persistedUser);
            return userMapper.toUserDto(updatedUser);
        }

        throw new UserException(UserExceptionErrorCode.USER_ERR_INVALID_NAME);
    }
}
