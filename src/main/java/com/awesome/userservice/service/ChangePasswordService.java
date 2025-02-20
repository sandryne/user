package com.awesome.userservice.service;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.UserRepository;
import com.awesome.userservice.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {

    private final UserRepository userRepository;


    public ChangePasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId, PasswordChangeRequest passwordChangeRequest) {
        User persistedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionErrorCode.USER_ERR_USER_NOT_FOUND));

        if (!PasswordUtil.checkPassword(passwordChangeRequest.getCurrentPassword(), persistedUser.getPassword())) {
            throw new UserException(UserExceptionErrorCode.USER_ERR_INVALID_PASSWORD);
        }
        persistedUser.setPassword(passwordChangeRequest.getNewPassword());
        userRepository.save(persistedUser);
    }


}
