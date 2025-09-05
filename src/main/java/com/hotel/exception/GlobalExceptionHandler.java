package com.hotel.exception;

import java.time.Instant;

import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    String msg = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> e.getField()+": "+e.getDefaultMessage()).findFirst().orElse("Validation error");
    return build(HttpStatus.BAD_REQUEST, "Validation error", msg, req.getRequestURI());
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiError> handleJakartaValidation(ValidationException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, "Validation error", ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> notFound(EntityNotFoundException ex, HttpServletRequest req) {
    return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<ApiError> forbidden(SecurityException ex, HttpServletRequest req) {
    return build(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> badRequest(IllegalArgumentException ex, HttpServletRequest req) {
    return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req.getRequestURI());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> generic(Exception ex, HttpServletRequest req) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", ex.getMessage(), req.getRequestURI());
  }

  private ResponseEntity<ApiError> build(HttpStatus status, String error, String message, String path) {
    return ResponseEntity.status(status)
        .body(new ApiError(Instant.now(), status.value(), error, message, path));
  }
}