package com.fleet.common.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseBuilder {

    public <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {

        ApiResponse<T> response = new ApiResponse<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);

    }

    public <T> ResponseEntity<ApiResponse<T>> failure(HttpStatus status, String message) {

        ApiResponse<T> response = new ApiResponse<>(status.value(), message);
        return new ResponseEntity<>(response, status);

    }

}
