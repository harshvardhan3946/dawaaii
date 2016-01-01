package com.dawaaii.web.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class DawaaiiApiResponse {

    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private static final String CONTENT_TYPE = "Content-Type";
    private final Map<String, Object> data = new HashMap<>();
    private final HttpStatus httpStatus;

    public DawaaiiApiResponse(String messageKey, String message, HttpStatus httpStatus) {
        addData(messageKey, message);
        this.httpStatus = httpStatus;
    }

    private void addData(String key, Object data) {
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(data)) return;
        this.data.put(key, data);
    }

    public DawaaiiApiResponse(HttpStatus httpStatus) {
        this("","",httpStatus);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public static DawaaiiApiResponse error(String message) {
        return error(message, BAD_REQUEST);
    }

    public static DawaaiiApiResponse error(String message, HttpStatus httpStatus) {
        return new DawaaiiApiResponse(ERROR, message, httpStatus);

    }

    public static DawaaiiApiResponse success(String message) {
        return new DawaaiiApiResponse(SUCCESS, message, OK);
    }

    public DawaaiiApiResponse withEntity(String entityKey, Object entity) {
        data.put(entityKey, entity);
        return this;
    }

    public ResponseEntity<DawaaiiApiResponse> respond() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(this, headers, httpStatus);
    }

    public static DawaaiiApiResponse success() {
        return new DawaaiiApiResponse(OK);
    }
}
