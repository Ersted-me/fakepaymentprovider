package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.dto.SecurePayment;
import net.ersted.fakepaymentprovider.dto.WebhookRq;
import net.ersted.fakepaymentprovider.entity.Webhook;
import net.ersted.fakepaymentprovider.enums.WebhookStatus;
import net.ersted.fakepaymentprovider.mapper.WebhookMapper;
import net.ersted.fakepaymentprovider.repository.WebhookRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final WebhookMapper webhookMapper;

    public Mono<Void> sendWebhook(SecurePayment securePayment) {
        Mono<WebhookRq> rqMono = Mono.just(webhookMapper.mapToRq(securePayment));
        Mono<ClientResponse> clientRsMono = this.send(rqMono, URI.create(securePayment.getNotificationUrl()));

        return Mono.zip(rqMono, clientRsMono)
                .flatMap(tuples -> {
                    WebhookRq rq = tuples.getT1();
                    String encodeRq = new String(Base64.getEncoder().encode(rq.toString().getBytes()), StandardCharsets.UTF_8);
                    ClientResponse clientResponse = tuples.getT2();
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    Webhook webhook = Webhook.builder()
                            .code(String.valueOf(clientResponse.statusCode().value()))
                            .url(securePayment.getNotificationUrl())
                            .request(encodeRq)
                            .response("test")
                            .numberRetry(0)
                            .lastRetryAt(currentDateTime)
                            .paymentId(securePayment.getTransactionId())
                            .createdAt(currentDateTime)
                            .createdBy("auto")
                            .updatedAt(currentDateTime)
                            .updatedBy("auto")
                            .status(clientResponse.statusCode().is2xxSuccessful()
                                    ? WebhookStatus.SUCCESS
                                    : WebhookStatus.FAILED)
                            .build();
                    return webhookRepository.save(webhook);
                })
                .then();
    }

    private Mono<ClientResponse> send(Mono<WebhookRq> rq, URI uri) {
        return WebClient.builder().build()
                .post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(rq, WebhookRq.class)
                .exchangeToMono(Mono::just);
    }
}