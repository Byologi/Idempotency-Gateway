package com.idempotency_gateway.exception;

import org.springframework.http.HttpStatus;
import com.idempotency_gateway.dto.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ExceptionHandler(
            IdempotencyConflictException.class
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(
            IdempotencyConflictException ex) {

        return ErrorResponse.builder()
                .error(ex.getMessage())
                .build();
    }
}