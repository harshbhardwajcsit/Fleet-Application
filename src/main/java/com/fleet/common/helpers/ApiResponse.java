package com.fleet.common.helpers;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private List<String> errors;

    public ApiResponse() {
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public ApiResponse(int statusCode, String message, List<String> errors) {
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;
    }
}
