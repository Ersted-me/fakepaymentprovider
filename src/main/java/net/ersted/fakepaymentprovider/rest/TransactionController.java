package net.ersted.fakepaymentprovider.rest;

import net.ersted.fakepaymentprovider.dto.CreatePaymentRs;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.GetTransactionListRs;
import net.ersted.fakepaymentprovider.dto.NonSecurePayment;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import net.ersted.fakepaymentprovider.service.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payments/transaction")
public class TransactionController {
    private final PaymentService paymentService;

    public TransactionController(@Qualifier("topUpService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Mono<CreatePaymentRs> create(@RequestBody CreateTopUpRq rq, ServerWebExchange exchange) {
        return paymentService.create(rq, PaymentType.TRANSACTION, (String) exchange.getAttributes().get("merchantId"));
    }

    @GetMapping("/list")
    public Mono<GetTransactionListRs<NonSecurePayment>> list(@RequestParam(name = "start_date", required = false) Optional<Instant> startDateTimePeriod,
                                                             @RequestParam(name = "end_date", required = false) Optional<Instant> endDateTimePeriod) {
        Instant start = startDateTimePeriod.orElseGet(() -> LocalDate.now().atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC));
        Instant end = endDateTimePeriod.orElseGet(() -> LocalDate.now().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
        return paymentService.findAllInNonSecureForm(PaymentType.TRANSACTION, start, end)
                .map(GetTransactionListRs::new);
    }

    @GetMapping("/{transactionId}/details")
    public Mono<NonSecurePayment> details(@PathVariable String transactionId) {
        return paymentService.findInNonSecureForm(transactionId, PaymentType.TRANSACTION);
    }
}