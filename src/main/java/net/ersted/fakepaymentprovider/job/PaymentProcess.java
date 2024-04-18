package net.ersted.fakepaymentprovider.job;

import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import net.ersted.fakepaymentprovider.service.PaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PaymentProcess {

    private final PaymentService transactionService;
    private final PaymentService payoutService;

    public PaymentProcess(@Qualifier("topUpService") PaymentService transactionService, @Qualifier("payoutService") PaymentService payoutService) {
        this.transactionService = transactionService;
        this.payoutService = payoutService;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public Mono<Void> transactionProcess() {
        return Flux.empty()
                .thenMany(transactionService.findAll(PaymentType.TRANSACTION, PaymentStatus.IN_PROCESS).flatMap(transactionService::approve))
                .subscribeOn(Schedulers.newBoundedElastic(5, 1000, "Scheduler-elastic-5-1000"))
                .then();
    }

    @Scheduled(cron = "*/10 * * * * *")
    public Mono<Void> payoutProcess() {
        return Flux.empty()
                .thenMany(payoutService.findAll(PaymentType.PAYOUT, PaymentStatus.IN_PROCESS).flatMap(transactionService::approve))
                .subscribeOn(Schedulers.newBoundedElastic(5, 1000, "Scheduler-elastic-5-1000"))
                .then();
    }
}