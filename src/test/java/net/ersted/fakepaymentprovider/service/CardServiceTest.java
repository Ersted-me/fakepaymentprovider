package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.CardRepository;
import net.ersted.fakepaymentprovider.util.CardDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.time.YearMonth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    TransactionalOperator transactionalOperator;
    @InjectMocks
    CardService cardService;

    @Test
    @DisplayName("Test find by id functionality")
    public void givenTransientCard_whenFind_thenCardIsReturned() {
        //given
        Card transientCard = CardDataUtils.getTransientCard();
        assert transientCard.getId() != null;

        BDDMockito.given(cardRepository.findById(transientCard.getId()))
                .willReturn(Mono.just(transientCard));
        //when
        StepVerifier.create(cardService.find(transientCard.getId()))
                //then
                .expectNext(transientCard)
                .expectComplete()
                .verify();
        verify(cardRepository, times(1)).findById(transientCard.getId());
    }

    @Test
    @DisplayName("Test find by id throw exception functionality")
    public void givenNonExistCardId_whenFind_thenExceptionIsThrown() {
        //given
        BDDMockito.given(cardRepository.findById(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(cardService.find(anyString()))
                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Card was not found")
                        && nfe.getStatus().equals("CARD_NOT_FOUND"))
                .verify();
        verify(cardRepository, times(1))
                .findById(anyString());
    }


    @Test
    @DisplayName("Test find by cardNumber, expDate, cvv functionality")
    public void givenCardNumberExpDateCVVCard_whenFind_thenCardIsReturned() {
        //given
        Card transientCard = CardDataUtils.getTransientCard();
        String cardNumber = transientCard.getCardNumber();
        YearMonth expDate = transientCard.getExpDate();
        String cvv = transientCard.getCvv();


        BDDMockito.given(cardRepository.findByCardNumberAndExpDateAndCvv(cardNumber, expDate, cvv))
                .willReturn(Mono.just(transientCard));
        //when
        StepVerifier.create(cardService.find(cardNumber, expDate, cvv))
                //then
                .expectNext(transientCard)
                .expectComplete()
                .verify();

        verify(cardRepository, times(1))
                .findByCardNumberAndExpDateAndCvv(cardNumber, expDate, cvv);
    }

    @Test
    @DisplayName("Test find by cardNumber, expDate, cvv throw exception functionality")
    public void givenNonExistCardNumberExpDateCVV_whenFind_thenExceptionIsThrown() {
        //given
        BDDMockito.given(cardRepository.findByCardNumberAndExpDateAndCvv(anyString(), any(), anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(cardService.find(anyString(), any(), anyString()))
                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Card was not found")
                        && nfe.getStatus().equals("CARD_NOT_FOUND"))
                .verify();
        verify(cardRepository, times(1))
                .findByCardNumberAndExpDateAndCvv(anyString(), any(), anyString());
    }
}