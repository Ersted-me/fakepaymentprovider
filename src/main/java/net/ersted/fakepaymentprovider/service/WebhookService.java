package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.dto.SecurePayment;
import net.ersted.fakepaymentprovider.entity.Webhook;
import net.ersted.fakepaymentprovider.enums.WebhookStatus;
import net.ersted.fakepaymentprovider.mapper.WebhookMapper;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import net.ersted.fakepaymentprovider.repository.WebhookRepository;
import net.ersted.fakepaymentprovider.util.AlgorithmUtils;
import net.ersted.fakepaymentprovider.util.JsonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {
    private final Integer MAXIMUM_NUMBER_OF_REPETITIONS_ALLOWED = 5;

    private final WebhookRepository webhookRepository;
    private final WebhookMapper webhookMapper;
    private final PaymentRepository paymentRepository;

    public Mono<Void> sendWebhook(SecurePayment securePayment) {
        Mono<String> rqMono = Mono.just(webhookMapper.mapToRq(securePayment)).flatMap(webhookRq -> Mono.just(JsonUtils.getJsonFromObject(webhookRq)));
        Mono<ClientResponse> clientRsMono = this.send(rqMono, URI.create(securePayment.getNotificationUrl()));

        return Mono.zip(rqMono, clientRsMono)
                .flatMap(tuples -> save(tuples.getT1(), tuples.getT2(), securePayment.getNotificationUrl(), securePayment.getTransactionId()))
                .then();
    }

    private Mono<Webhook> save(String rq, ClientResponse clientResponse, String URL, String transactionId) {
        String encodeRq = new String(Base64.getEncoder().encode(rq.getBytes()), StandardCharsets.UTF_8);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Webhook webhook = Webhook.builder()
                .code(String.valueOf(clientResponse.statusCode().value()))
                .url(URL)
                .request(encodeRq)
                .response("test")
                .numberRetry(0)
                .lastRetryAt(currentDateTime)
                .paymentId(transactionId)
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(clientResponse.statusCode().is2xxSuccessful()
                        ? WebhookStatus.SUCCESS
                        : WebhookStatus.FAILED)
                .build();
        return webhookRepository.save(webhook);
    }

    public Mono<Webhook> processFailedWebhook(Webhook webhook) {
        Integer numberRetry = webhook.getNumberRetry();
        LocalDateTime lastRetryAt = webhook.getLastRetryAt();
        LocalDateTime nextRetryDateTime = lastRetryAt.plusMinutes(AlgorithmUtils.fibonacci(numberRetry));
        if (LocalDateTime.now().isBefore(nextRetryDateTime)) {
            return Mono.empty();
        }
        return paymentRepository.findByTransactionId(webhook.getPaymentId())
                .flatMap(payment -> {
                    String jsonRq = new String(Base64.getDecoder().decode(webhook.getRequest().getBytes()), StandardCharsets.UTF_8);
                    return send(Mono.just(jsonRq), URI.create(payment.getNotificationUrl()));
                })
                .flatMap(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        webhook.setNumberRetry(webhook.getNumberRetry() + 1);
                    }
                    webhook.setCode(String.valueOf(clientResponse.statusCode().value()));
                    webhook.setLastRetryAt(LocalDateTime.now());
                    webhook.setResponse("updated response at :" + webhook.getLastRetryAt());
                    return webhookRepository.save(webhook);
                });

    }

    private Mono<ClientResponse> send(Mono<String> rq, URI uri) {
        return WebClient.builder().build()
                .post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(rq, String.class)
                .exchangeToMono(Mono::just);
    }

    public Flux<Webhook> findAllFailedWebhooks() {
        return webhookRepository.findFailedWebhooks();
    }
}