package com.awesome.userservice.service;

import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.util.UserMapper;
import com.awesome.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RetrieveUserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public RetrieveUserService(UserMapper userMapper,
                               UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUserDto);
    }
}
