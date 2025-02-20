package com.awesome.userservice.exception;

/**
 * This class regroup constants variable representing all the error throw by the user service.
 * The naming convention is : the service name + 'ERR'+ details about the thrown exception
 **/
public class UserExceptionErrorCode {
    public static final String USER_ERR_USER_NOT_FOUND = "USER_ERR_USER_NOT_FOUND";

    public static final String USER_ERR_INVALID_PASSWORD = "USER_ERR_INVALID_PASSWORD";
    public static final String USER_ERR_DUPLICATED_EMAIL = "USER_ERR_DUPLICATED_EMAIL";
    public static final String USER_ERR_INVALID_NAME = "USER_ERR_INVALID_NAME";
}
