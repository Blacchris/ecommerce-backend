package com.springdev.Validators;


import com.springdev.CustomExceptions.AdminNotFoundException;
import com.springdev.CustomExceptions.AlreadySellerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> HandleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("timeStamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("error", "Validation Error");
        response.put("details", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<Map<String, Object>> handleBadCredentialException(BadCredentialsException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getLocalizedMessage());
        response.put("status", HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<Map<String, Object>> HandleAdminNotFoundException(AdminNotFoundException ex){
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        Map<String, Object> response = new HashMap<>();

        response.put("timeStamp", Instant.now());
        response.put("status", HttpStatus.FORBIDDEN);
        response.put("details", error);
        return ResponseEntity.badRequest().body(response);

    }

    @ExceptionHandler(AlreadySellerException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadySellerException(AlreadySellerException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("timeStamp", Instant.now());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("details", error);

        return ResponseEntity.badRequest().body(response);
    }




}
