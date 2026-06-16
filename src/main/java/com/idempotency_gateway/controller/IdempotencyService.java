package com.idempotency_gateway.controller;

import com.idempotency_gateway.repository.IdempotencyRepository;
import com.idempotency_gateway.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final PaymentService paymentService;

}
