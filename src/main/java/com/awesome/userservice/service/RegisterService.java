package com.awesome.userservice.service;

import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.model.Role;
import com.awesome.userservice.repository.RoleRepository;
import com.awesome.userservice.util.PasswordUtil;
import com.awesome.userservice.util.UserMapper;
import com.awesome.userservice.exception.UserException;
import com.awesome.userservice.exception.UserExceptionErrorCode;
import com.awesome.userservice.model.User;
import com.awesome.userservice.repository.UserRepository;
import com.awesome.userservice.util.UserRoleEnum;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final EmailService emailService;


    public RegisterService(UserMapper userMapper,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           EmailService emailService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    public UserDto execute(UserDto userInputDto) {
        doSemanticCheck(userInputDto);

        User savedUser = createUser(userInputDto);
        sendEmail(savedUser);

        return userMapper.toUserDto(savedUser);
    }

    private void sendEmail(User savedUser) {
        //todo externalize subject and content
        String subject = "Welcome to Our Marketplace!";
        String content = "<h1>Hello, " + savedUser.getName() + "!</h1><p>Thank you for registering.</p>";

        try {
            emailService.sendEmail(savedUser.getEmail(), subject, content);
        } catch (MessagingException exception) {
            throw new UserException(UserExceptionErrorCode.USER_ERR_DUPLICATED_EMAIL);
        }

    }

    private User createUser(UserDto userInputDto) {
        User user = userMapper.toUser(userInputDto);
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setVerified(false);

        Role userRole = roleRepository.findByName(UserRoleEnum.ROLE_USER.toString())
                .orElseGet(() -> roleRepository.save(new Role(UserRoleEnum.ROLE_USER.toString())));
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }

    private void doSemanticCheck(UserDto userInputDto) {
        if (userRepository.findByEmail(userInputDto.getEmail()).isPresent()) {
            throw new UserException(UserExceptionErrorCode.USER_ERR_DUPLICATED_EMAIL);
        }
    }

}
