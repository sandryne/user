package com.awesome.userservice.service;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final RegisterService registerService;
    private final RetrieveUserService retrieveUserService;
    private final UpdateUserService updateUserService;
    private final ChangePasswordService changePasswordService;

    @Autowired
    public UserServiceImpl(RegisterService registerService,
                           RetrieveUserService retrieveUserService,
                           UpdateUserService updateUserService,
                           ChangePasswordService changePasswordService) {
        this.registerService = registerService;
        this.retrieveUserService = retrieveUserService;
        this.updateUserService = updateUserService;
        this.changePasswordService = changePasswordService;
    }

    @Override
    public UserDto registerUser(UserDto userDto) {
        return registerService.execute(userDto);

    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return retrieveUserService.findByEmail(email);
    }

    @Override
    public UserDto updateProfile(UserDto userDto) {
        //todo check connected user is the userDto OR ADMIN
        return updateUserService.execute(userDto);
    }

    @Override
    public void changePassword(Long userId, PasswordChangeRequest passwordChangeRequest) {
        //todo check connected user is the userDto or ADMIN
        changePasswordService.execute(userId,passwordChangeRequest);
    }

}

