package com.dawaaii.web.common.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class DawaaiiFieldError {
    private final String errorCode;

    private final String field;

    public DawaaiiFieldError(FieldError fieldError) {
        field = fieldError.getField();
        errorCode = fieldError.getObjectName()+"."+field+"."+fieldError.getCode();
    }

    public static List<DawaaiiFieldError> parseErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DawaaiiFieldError::new)
                .collect(Collectors.toList());
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }
}
