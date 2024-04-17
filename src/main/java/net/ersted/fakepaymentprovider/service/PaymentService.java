package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.dto.*;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

public interface PaymentService {
    Mono<CreatePaymentRs> create(CreateTopUpRq dto, PaymentType type, String merchantId);
    Mono<Payment> approve(Payment payment);
    Mono<NonSecurePayment> findInNonSecureForm(String transactionId, PaymentType type);
    Mono<SecurePayment> findInSecureForm(String transactionId, PaymentType type);
    Mono<List<NonSecurePayment>> findAllInNonSecureForm(PaymentType paymentType, Instant start, Instant end);
    Mono<List<SecurePayment>> findAllInSecureForm(PaymentType paymentType, Instant start, Instant end);
    Flux<Payment> findAll(PaymentType paymentType, PaymentStatus status);
}