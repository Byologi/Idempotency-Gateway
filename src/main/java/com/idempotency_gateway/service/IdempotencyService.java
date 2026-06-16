package com.idempotency_gateway.service;

import com.idempotency_gateway.dto.IdempotencyResult;
import com.idempotency_gateway.dto.PaymentRequest;
import com.idempotency_gateway.dto.PaymentResponse;
import com.idempotency_gateway.entity.IdempotencyRecord;
import com.idempotency_gateway.entity.IdempotencyStatus;
import com.idempotency_gateway.exception.IdempotencyConflictException;
import com.idempotency_gateway.repository.IdempotencyRepository;
import com.idempotency_gateway.util.PayloadHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final PaymentService paymentService;
    private final RequestCoordinator requestCoordinator;

    @Transactional
    public IdempotencyResult process(
            String idempotencyKey,
            PaymentRequest request
    ) throws InterruptedException {

        String payloadHash =
                PayloadHashUtil.hash(
                        request.getAmount()
                                + ":" +
                                request.getCurrency()
                );

        CompletableFuture<IdempotencyResult> inFlight =
                requestCoordinator.get(idempotencyKey);

        if (inFlight != null) {

            IdempotencyResult result =
                    inFlight.join();

            return IdempotencyResult.builder()
                    .response(result.getResponse())
                    .statusCode(result.getStatusCode())
                    .cacheHit(true)
                    .build();
        }

        Optional<IdempotencyRecord> existing =
                repository.findByIdempotencyKey(
                        idempotencyKey
                );

        if (existing.isPresent()) {

            IdempotencyRecord record =
                    existing.get();

            if (!record.getPayloadHash()
                    .equals(payloadHash)) {

                throw new IdempotencyConflictException(
                        "Idempotency key already used for a different request body."
                );
            }

            return IdempotencyResult.builder()
                    .response(
                            PaymentResponse.builder()
                                    .message(
                                            record.getResponseBody()
                                    )
                                    .build()
                    )
                    .statusCode(
                            record.getStatusCode()
                    )
                    .cacheHit(true)
                    .build();
        }

        CompletableFuture<IdempotencyResult> future =
                new CompletableFuture<>();

        requestCoordinator.put(
                idempotencyKey,
                future
        );

        try {

            PaymentResponse response =
                    paymentService.processPayment(
                            request
                    );

            IdempotencyRecord record =
                    new IdempotencyRecord();

            record.setIdempotencyKey(
                    idempotencyKey
            );

            record.setPayloadHash(
                    payloadHash
            );

            record.setStatus(
                    IdempotencyStatus.COMPLETED
            );

            record.setStatusCode(201);

            record.setResponseBody(
                    response.getMessage()
            );

            repository.save(record);

            IdempotencyResult result =
                    IdempotencyResult.builder()
                            .response(response)
                            .statusCode(201)
                            .cacheHit(false)
                            .build();

            future.complete(result);

            return result;

        } catch (Exception ex) {

            future.completeExceptionally(ex);

            throw ex;

        } finally {

            requestCoordinator.remove(
                    idempotencyKey
            );
        }
    }
}
