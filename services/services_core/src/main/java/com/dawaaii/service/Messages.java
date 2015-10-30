package com.dawaaii.service;

public interface Messages {
    String USER_EMAIL_ALREADY_EXISTS = "email already exists";
    String USER_NAME_ALREADY_EXISTS = "username already exists";
    String USER_CREATED_SUCCESSFULLY = "user created successfully";
    String USER_UPDATED_SUCCESSFULLY = "user updated successfully";
    String USER_NOT_FOUND = "user not found";
    String SERVER_ERROR = "server error";
    String OPERATION_SUCCESS = "operation successful";
    String OPERATION_ERROR = "operation error";
    String OPERATION_NOT_ALLOWED = "operation not allowed";
    String INVALID_REQUEST = "invalid request";
    String EMAIL_CONFIRMED = "email confirmed";
    String PASSWORD_RESET_SUCCESSFULLY = "password reset successfully";
    String EMAIL_NOT_CONFIRMED = "email not confirmed";
    String EMAIL_NOT_EXISTS = "email does not exists";
    String INVALID_OTP = "invalid otp";
    String OTP_SENT = "otp sent";
    String INVALID_USER_PASSWORD = "wrong username/password";
    String OTP_NOT_EXIST = "otp does not exist";
    String OTP_REQUEST_LIMIT_EXCEEDED = "otp request limit exceeded for day";
}
