package com.example.uploadbucket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse<T> extends ResponseEntity<T> {

    public ApiResponse(HttpStatus status) {
        super(status);
    }

    public ApiResponse(T body, HttpStatus status) {
        super(body, status);
    }

    public ApiResponse(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public ApiResponse(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

}
