package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.enums.CardStatus;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final TransactionalOperator transactionalOperator;


    public Mono<Card> save(String cardNumber, YearMonth expDate, String cvv, String customerId) {
        return Mono.just(Card.builder()).flatMap(cardBuilder -> {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    return cardRepository.save(cardBuilder
                            .cardNumber(cardNumber)
                            .expDate(expDate)
                            .cvv(cvv)
                            .customerId(customerId)
                            .balance(BigDecimal.valueOf(Math.ceil(Math.random() * (1000 - 100)) + 100))
                            .createdAt(currentDateTime)
                            .createdBy("auto")
                            .updatedAt(currentDateTime)
                            .updatedBy("auto")
                            .status(CardStatus.ACTIVE)
                            .build());
                })
                .as(transactionalOperator::transactional)
                .doOnSuccess(card -> log.info("update card: {}", card.toString()));
    }

    public Mono<Card> save(Card card) {
        return cardRepository.save(card)
                .as(transactionalOperator::transactional)
                .doOnSuccess(c -> log.info("update card: {}", c.toString()));
    }

    public Mono<Card> find(String cardId) {
        return cardRepository.findById(cardId)
                .switchIfEmpty(Mono.error(new NotFoundException("CARD_NOT_FOUND", "Card was not found")));
    }

    public Mono<Card> find(String cardNumber, YearMonth expDate, String cvv) {
        return cardRepository.findByCardNumberAndExpDateAndCvv(cardNumber, expDate, cvv)
                .switchIfEmpty(Mono.error(new NotFoundException("CARD_NOT_FOUND", "Card was not found")));
    }
}