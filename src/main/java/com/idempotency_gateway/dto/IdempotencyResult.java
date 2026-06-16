package com.idempotency_gateway.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdempotencyResult {

    private PaymentResponse response;

    private Integer statusCode;

    private boolean cacheHit;
}