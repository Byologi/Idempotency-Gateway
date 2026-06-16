package com.idempotency_gateway.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String message;
}