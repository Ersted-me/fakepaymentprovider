package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Card;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

public interface CardRepository extends R2dbcRepository<Card, String> {
    Mono<Card> findByCardNumberAndExpDateAndCvv(String cardNumber, YearMonth expDate, String cvv);
}
