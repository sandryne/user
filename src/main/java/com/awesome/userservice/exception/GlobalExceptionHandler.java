package com.awesome.userservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //todo Adapt the method to fetch from db the error message text and  http status
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleRuntimeException(UserException ex) {

        if (UserExceptionErrorCode.USER_ERR_DUPLICATED_EMAIL.equals(ex.getErrorCode())) {
            return new ResponseEntity<>(ex.getErrorCode(), HttpStatus.CONFLICT);
        }
        if (UserExceptionErrorCode.USER_ERR_USER_NOT_FOUND.equals(ex.getErrorCode())) {
            return new ResponseEntity<>(ex.getErrorCode(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
