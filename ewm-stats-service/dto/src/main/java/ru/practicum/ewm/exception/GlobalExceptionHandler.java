package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        return new ResponseEntity<>(Map.of(
                "status", status,
                "message", message
        ), status);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status,
                                                       String message,
                                                       Object payload) {
        return new ResponseEntity<>(Map.of(
                "status", status,
                "message", message,
                "payload", payload
        ), status);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Object> handleClientException(ClientException ex, WebRequest request) {
        log.warn("Client exception occurred: {}", ex.getMessage());

        if (log.isDebugEnabled()) {
            log.debug("Stack trace:", ex);
        }

        return buildResponseEntity(
                ex.getHttpStatus(),
                ex.getClass().getName(),
                ex.getApiMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Validation errors occurred: {}", errors);

        return buildResponseEntity(
                HttpStatus.BAD_REQUEST,
                "validation errors occurred",
                errors
        );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleRuntimeException(Throwable ex, WebRequest request) {
        log.error("Internal server error: ", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "something went wrong");
    }
}
