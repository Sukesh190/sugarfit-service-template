package com.sugarfit.platform.servicetemplate.exception;

import java.time.Instant;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.sugarfit.platform.servicetemplate.constants.AppConstants;
import com.sugarfit.platform.servicetemplate.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid fails — return per-field errors so the client knows what to fix
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getBindingResult().getFieldErrors());
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(
                fe -> ErrorResponse.FieldError.builder().field(fe.getField()).message(fe.getDefaultMessage()).build())
                .toList();
        ErrorResponse errorResponse = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST.name())
                .message("Request body contains invalid or missing fields")
                .timestamp(Instant.now())
                .requestId(MDC.get(AppConstants.REQUEST_ID_MDC_KEY))
                .fieldErrors(fieldErrors).build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // malformed JSON, wrong content type, etc.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed request body: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .message("Malformed or unreadable request body")
                .requestId(MDC.get(AppConstants.REQUEST_ID_MDC_KEY))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // catch-all — anything not handled above lands here
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("An unexpected error occurred. Please try again later.")
                .requestId(MDC.get(AppConstants.REQUEST_ID_MDC_KEY))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("Method not supported: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.name())
                .message(ex.getMessage())
                .requestId(MDC.get(AppConstants.REQUEST_ID_MDC_KEY))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Resource not found: {}", ex.getResourcePath());
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .message("The requested resource was not found")
                .requestId(MDC.get(AppConstants.REQUEST_ID_MDC_KEY))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
