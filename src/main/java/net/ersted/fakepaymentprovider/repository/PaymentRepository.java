package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends R2dbcRepository<Payment, String> {
    Mono<Payment> findByTransactionIdAndType(String transactionId, PaymentType type, String account_merchant_id);
    Mono<Payment> findByTransactionIdAndType(String id, PaymentType type);
    Flux<Payment> findAllByTypeAndCreatedAtBetween(PaymentType type, Instant createdAt, Instant createdAt2);
    Flux<Payment> findAllByTypeAndStatus(PaymentType type, PaymentStatus status);


}
