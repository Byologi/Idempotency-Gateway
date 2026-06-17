package com.idempotency_gateway.exception;

import org.springframework.http.HttpStatus;
import com.idempotency_gateway.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return errors;
    }
    @ExceptionHandler(IdempotencyConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(
            IdempotencyConflictException ex
    ) {

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                ex.getMessage()
        );

        return error;
    }
    @ExceptionHandler(
            MissingRequestHeaderException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingHeader(
            MissingRequestHeaderException ex
    ) {

        Map<String, String> error =
                new HashMap<>();

        error.put(
                ex.getHeaderName(),
                ex.getHeaderName() + " header is required"
        );

        return error;
    }
}