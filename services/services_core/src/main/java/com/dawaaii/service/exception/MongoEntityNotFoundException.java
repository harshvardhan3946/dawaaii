package com.dawaaii.service.exception;

public class MongoEntityNotFoundException extends RuntimeException{
    private String errorCode;

    public MongoEntityNotFoundException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
