package com.idempotency_gateway.controller;

import com.idempotency_gateway.dto.PaymentRequest;
import com.idempotency_gateway.dto.PaymentResponse;
import com.idempotency_gateway.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/process-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse processPayment(
            @Valid @RequestBody PaymentRequest request
    ) throws InterruptedException {

        return service.processPayment(request);
    }
}