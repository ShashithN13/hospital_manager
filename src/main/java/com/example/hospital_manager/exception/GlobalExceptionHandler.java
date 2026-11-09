package com.example.hospital_manager.exception;

import com.example.hospital_manager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex, HttpServletRequest req) {
        return response(ex.getStatus(), ex.getMessage(), req);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
          .map(e -> e.getField() + ": " + e.getDefaultMessage()).findFirst().orElse("Validation failed");
        return response(HttpStatus.BAD_REQUEST, message, req);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest req) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage() == null ? "Unexpected error" : ex.getMessage(), req);
    }
    private ResponseEntity<ErrorResponse> response(HttpStatus status, String msg, HttpServletRequest req) {
        return ResponseEntity.status(status).body(new ErrorResponse(LocalDateTime.now(), status.value(),
          status.getReasonPhrase(), msg, req.getRequestURI()));
    }
}
