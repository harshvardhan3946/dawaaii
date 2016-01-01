package com.dawaaii.service.exception;

public class CoreException extends RuntimeException{

    private String errorCode;

    public CoreException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
