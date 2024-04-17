package net.ersted.fakepaymentprovider.rest;

import net.ersted.fakepaymentprovider.dto.CreatePaymentRs;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.GetPayoutListRs;
import net.ersted.fakepaymentprovider.dto.SecurePayment;
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
@RequestMapping("/api/v1/payments/payout")
public class PayoutController {
    private final PaymentService paymentService;

    public PayoutController(@Qualifier("payoutService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Mono<CreatePaymentRs> create(@RequestBody CreateTopUpRq rq, ServerWebExchange exchange) {
        return paymentService.create(rq, PaymentType.PAYOUT, (String) exchange.getAttributes().get("merchantId"));
    }

    @GetMapping("/list")
    public Mono<GetPayoutListRs<SecurePayment>> list(@RequestParam(name = "start_date", required = false) Optional<Instant> startDateTimePeriod,
                                                     @RequestParam(name = "end_date", required = false) Optional<Instant> endDateTimePeriod) {
        Instant start = startDateTimePeriod.orElseGet(() -> LocalDate.now().atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC));
        Instant end = endDateTimePeriod.orElseGet(() -> LocalDate.now().atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
        return paymentService.findAllInSecureForm(PaymentType.PAYOUT, start, end)
                .map(GetPayoutListRs::new);
    }

    @GetMapping("/{transactionId}/details")
    public Mono<SecurePayment> details(@PathVariable String transactionId) {
        return paymentService.findInSecureForm(transactionId, PaymentType.PAYOUT);
    }
}