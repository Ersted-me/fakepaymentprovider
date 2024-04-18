package net.ersted.fakepaymentprovider.rest;

import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.dto.WebhookRq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@Slf4j
public class WebhookController {

    @PostMapping("/webhook/transaction")
    public Mono<String> transaction(@RequestBody WebhookRq rq) {
        return Mono.just("transaction webhook has been received")
                .doOnSuccess(s -> log.info(rq.toString()));
    }

    @PostMapping("/webhook/payout")
    public Mono<String> payout(@RequestBody WebhookRq rq) {
        return Mono.just("payout webhook has been received")
                .doOnSuccess(s -> log.info(rq.toString()));
    }
}