package com.idempotency_gateway.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestCoordinator {

    private final ConcurrentHashMap<String, CompletableFuture<Void>>
            inFlightRequests = new ConcurrentHashMap<>();

    public CompletableFuture<Void> get(String key) {
        return inFlightRequests.get(key);
    }

    public void put(
            String key,
            CompletableFuture<Void> future
    ) {
        inFlightRequests.put(key, future);
    }

    public void remove(String key) {
        inFlightRequests.remove(key);
    }
}