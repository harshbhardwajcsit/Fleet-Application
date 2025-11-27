package com.fleet.common.helpers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fleet.common.exceptions.ConflictException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseBuilder responseBuilder;


    // Exception Handler for Input Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<Void> response = new ApiResponse<>(400, "Validation failed", errorMessages);

        return ResponseEntity.badRequest().body(response);
    }

    // Exception Handler for Conflict Exceptions
     @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiResponse<Object>> handleConflict(RuntimeException ex) {
        return responseBuilder.failure(HttpStatus.CONFLICT, ex.getMessage());
    
    }

    // Generic Exception Handler for un-checked exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return responseBuilder.failure(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}