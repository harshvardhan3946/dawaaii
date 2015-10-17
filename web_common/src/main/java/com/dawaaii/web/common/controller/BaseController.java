package com.dawaaii.web.common.controller;

import com.dawaaii.service.exception.CoreException;
import com.dawaaii.web.common.response.DawaaiiApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.dawaaii.web.common.response.DawaaiiApiResponse.error;

public class BaseController {
    public static final String ACCEPT_JSON = "Accept=application/json";
    public static final String ERRORS = "errors";

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<DawaaiiApiResponse> handleException(CoreException exception) {
        return error(exception.getMessage()).respond();
    }
}
