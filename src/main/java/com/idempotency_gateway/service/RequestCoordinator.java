package com.idempotency_gateway.service;

import com.idempotency_gateway.dto.IdempotencyResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestCoordinator {

    private final ConcurrentHashMap<String, CompletableFuture<IdempotencyResult>>
            inFlightRequests = new ConcurrentHashMap<>();

    public CompletableFuture<IdempotencyResult> get(String key) {
        return inFlightRequests.get(key);
    }

    public void put(
            String key,
            CompletableFuture<IdempotencyResult> future
    ) {
        inFlightRequests.put(key, future);
    }

    public void remove(String key) {
        inFlightRequests.remove(key);
    }
}