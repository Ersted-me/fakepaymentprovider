package net.ersted.fakepaymentprovider.it;

import net.ersted.fakepaymentprovider.config.PostgresqlContainerConfig;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import net.ersted.fakepaymentprovider.util.TransactionDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgresqlContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItTransactionRestControllerTests {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Test create transaction functionality")
    public void givenTransactionDto_whenCreateTransaction_thenSuccessResponse(){
        //given
        CreateTopUpRq bobFirsty = TransactionDataUtils.createTopUpRqBobFirsty();
        //when
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/v1/payments/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Base64 UFJPU0VMWVRFOmIyZWVlYTNlMjc4MzRiNzQ5OWRkN2UwMTE0M2EyM2Rk")
                .body(Mono.just(bobFirsty), CreateTopUpRq.class)
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.transaction_id").isNotEmpty()
                .jsonPath("$.status").isEqualTo("IN_PROCESS")
                .jsonPath("$.message").isEqualTo("OK");
    }
}