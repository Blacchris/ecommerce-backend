package com.example.ecommerce.common.exception;

import com.example.ecommerce.common.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                               HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                                                                        HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

//    @ExceptionHandler(UserNotFoundExecption.class)
//    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(execption.getMessage());
//    }
//
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }
//
//    @ExceptionHandler(CartNotFoundException.class)
//    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }
//
//    @ExceptionHandler(CartItemNotFoundException.class)
//    public ResponseEntity<String> handleProductNotFoundException(CartItemNotFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }

//    @ExceptionHandler(OrderNotFoundException.class)
//    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException exception){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }

    @ExceptionHandler({
            UserNotFoundException.class, ProductNotFoundException.class,
            CartNotFoundException.class,CartItemNotFoundException.class,
            OrderNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException exception, WebRequest request){
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getDescription(false).replace("url","")
        );
          return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotCancelled.class)
    public ResponseEntity<ApiErrorResponse> handleOrderNotCancelExceptions(RuntimeException exception, WebRequest request){
        ApiErrorResponse response  = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
        exception.getMessage(),
        request.getDescription(false).replace("url","")
                        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex,
                                                           HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), ex);
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException ex,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
