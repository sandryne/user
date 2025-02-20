package com.awesome.userservice.controller;

import com.awesome.userservice.dto.PasswordChangeRequest;
import com.awesome.userservice.dto.UserDto;
import com.awesome.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Service", description = "API for managing users of an online marketplace")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Registers a user and sends an email verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Parameter(description = "User details for registration", required = true)
                                                @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userDto));
    }

    @Operation(summary = "Get user by email", description = "Fetches a user using their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@Parameter(description = "Email of the user to retrieve", required = true)
                                                  @PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user profile", description = "Allows a user to update their profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "The profile wasn't updated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateProfile(userDto));
    }

    @Operation(summary = "Change user password", description = "Allows a user to update their password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid current password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @Parameter(description = "Email, current password, and new password", required = true)
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest) {
        userService.changePassword(userId, passwordChangeRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

}
