package com.idempotency_gateway.exception;

public class IdempotencyConflictException
        extends RuntimeException {

    public IdempotencyConflictException(
            String message) {

        super(message);
    }
}