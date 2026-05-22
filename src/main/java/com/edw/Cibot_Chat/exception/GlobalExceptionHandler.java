package com.edw.Cibot_Chat.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handlerNotFound(ResourceNotFoundException ex, HttpServletRequest req ){
        ApiError body = new ApiError("NOT_FOUND", ex.getMessage(), Instant.now(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        Map<String, String> fields = new HashMap<>();

        for (FieldError fe: ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("code", "VALIDATION_ERROR");
        body.put("message", "Reques validation failed");
        body.put("timestamp", Instant.now());
        body.put("path", req.getRequestURI());
        body.put("fields", fields);

        return ResponseEntity.badRequest().body(body);
    }
    
}
