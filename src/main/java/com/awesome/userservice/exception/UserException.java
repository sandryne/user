package com.awesome.userservice.exception;

public class UserException extends RuntimeException {
    private String errorCode;

    public UserException() {
        super();
    }

    public UserException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
