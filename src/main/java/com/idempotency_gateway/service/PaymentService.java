package com.idempotency_gateway.service;

import com.idempotency_gateway.dto.PaymentRequest;
import com.idempotency_gateway.dto.PaymentResponse;
import com.idempotency_gateway.repository.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IdempotencyRepository repository;

    public PaymentResponse processPayment(
            PaymentRequest request
    ) throws InterruptedException {

        Thread.sleep(2000);

        return PaymentResponse.builder()
                .message(
                        "Charged "
                                + request.getAmount()
                                + " "
                                + request.getCurrency()
                )
                .build();
    }
}