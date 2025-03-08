package com.example.demotrungtam.exception;

import com.example.demotrungtam.common.Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(value = {CustomException.class})
    ResponseEntity<Object> handleCustomException(CustomException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> message = new HashMap<>();
        message.put("code", ex.getCode());
        message.put("params", ex.getMessage());
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {CustomNotFoundException.class})
    ResponseEntity<Object> handleCustomNotFoundException(CustomNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.value());
        Map<String, Object> message = new HashMap<>();
        message.put("code", ex.getCode());
        message.put("params", ex.getMessage());
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
