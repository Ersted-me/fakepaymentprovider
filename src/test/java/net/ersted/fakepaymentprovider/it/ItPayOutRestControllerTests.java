package net.ersted.fakepaymentprovider.it;

import net.ersted.fakepaymentprovider.config.PostgresqlContainerConfig;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.entity.Merchant;
import net.ersted.fakepaymentprovider.enums.AccountStatus;
import net.ersted.fakepaymentprovider.enums.MerchantStatus;
import net.ersted.fakepaymentprovider.repository.AccountRepository;
import net.ersted.fakepaymentprovider.repository.MerchantRepository;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import net.ersted.fakepaymentprovider.util.TransactionDataUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(PostgresqlContainerConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ItPayOutRestControllerTests {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void init(){
        //PROSELYTE,b2eeea3e27834b7499dd7e01143a23dd,2024-03-25 19:00:00.000000,manual,2024-03-25 19:00:00.000000,manual,ACTIVE
        Merchant merchant = new Merchant("PROSELYTE", "b2eeea3e27834b7499dd7e01143a23dd", LocalDateTime.now(), "it test", LocalDateTime.now(), "it test", MerchantStatus.ACTIVE, null);
        Merchant savedMerchant = merchantRepository.save(merchant).block();
        Account account = new Account(null, "RUB", new BigDecimal(1000), "PROSELYTE", LocalDateTime.now(), "it test", LocalDateTime.now(), "it test", AccountStatus.ACTIVE, null, null);
        Account savedAccount = accountRepository.save(account).block();

    }
    @Test
    @DisplayName("Test failed create payOut functionality")
    public void givenInvalidPayoutDto_whenCreateTransaction_thenSuccessResponse(){
        //given
        CreateTopUpRq payOutBobFirsty = TransactionDataUtils.createTopUpRqBobFirsty();

        //when
        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/v1/payments/payout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Base64 UFJPU0VMWVRFOmIyZWVlYTNlMjc4MzRiNzQ5OWRkN2UwMTE0M2EyM2Rk")
                .body(Mono.just(payOutBobFirsty), CreateTopUpRq.class)
                .exchange();
        //then
        response.expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.status").isEqualTo("FAILED")
                .jsonPath("$.message").isEqualTo("PAYMENT_METHOD_NOT_ALLOWED");
    }
}
