package com.idempotency_gateway.controller;

import com.idempotency_gateway.dto.IdempotencyResult;
import com.idempotency_gateway.dto.PaymentRequest;
import com.idempotency_gateway.dto.PaymentResponse;
import com.idempotency_gateway.service.IdempotencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final IdempotencyService idempotencyService;

    @PostMapping("/process-payment")
    public ResponseEntity<PaymentResponse> processPayment(

            @RequestHeader("Idempotency-Key")
            String idempotencyKey,

            @Valid
            @RequestBody
            PaymentRequest request

    ) throws InterruptedException {

        IdempotencyResult result =
                idempotencyService.process(
                        idempotencyKey,
                        request
                );

        HttpHeaders headers =
                new HttpHeaders();

        if (result.isCacheHit()) {
            headers.add(
                    "X-Cache-Hit",
                    "true"
            );
        }

        return ResponseEntity
                .status(result.getStatusCode())
                .headers(headers)
                .body(result.getResponse());
    }
}