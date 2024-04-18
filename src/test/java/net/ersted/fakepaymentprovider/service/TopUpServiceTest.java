package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.dto.NonSecurePayment;
import net.ersted.fakepaymentprovider.dto.SecurePayment;
import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.entity.Customer;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import net.ersted.fakepaymentprovider.mapper.PaymentMapper;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import net.ersted.fakepaymentprovider.util.AccountDataUtils;
import net.ersted.fakepaymentprovider.util.CardDataUtils;
import net.ersted.fakepaymentprovider.util.CustomerDataUtils;
import net.ersted.fakepaymentprovider.util.PaymentDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class TopUpServiceTest {

    @Mock
    CardService cardService;
    @Mock
    CustomerService customerService;
    @Mock
    AccountService accountService;
    @Mock
    PaymentMapper paymentMapper;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    TopUpService topUpService;

    @Test
    @DisplayName("Test find payment in non secure form")
    public void givenTransactionIdPaymentType_whenFind_thenNonSecurePaymentIsReturned() {
        //given
        Payment transientPayment = PaymentDataUtils.getTransientTransactionPayment();
        Card transientCard = CardDataUtils.getTransientCard();
        Customer transientCustomer = CustomerDataUtils.getTransientCustomer();
        Account transientMerchantRubAccount = AccountDataUtils.getTransientMerchantRubAccount();
        BDDMockito.given(paymentRepository.findByTransactionIdAndType(transientPayment.getId(), PaymentType.TRANSACTION))
                        .willReturn(Mono.just(transientPayment));
        BDDMockito.given(cardService.find(transientPayment.getCardId()))
                .willReturn(Mono.just(transientCard));
        BDDMockito.given(customerService.findById(transientCard.getCustomerId()))
                .willReturn(Mono.just(transientCustomer));
        BDDMockito.given(accountService.find(transientPayment.getAccountId()))
                .willReturn(Mono.just(transientMerchantRubAccount));

        NonSecurePayment mappedDto = new NonSecurePayment();
        BDDMockito.given(paymentMapper.mapToNonSecurePayment(transientPayment))
                .willReturn(mappedDto);
        //when
        StepVerifier.create(topUpService.findInNonSecureForm(transientPayment.getTransactionId(),PaymentType.TRANSACTION))
                //then
                .expectNext(mappedDto)
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("Test find payment in secure form")
    public void givenTransactionIdPaymentType_whenFind_thenSecurePaymentIsReturned() {
        //given
        Payment transientPayment = PaymentDataUtils.getTransientTransactionPayment();
        Card transientCard = CardDataUtils.getTransientCard();
        Customer transientCustomer = CustomerDataUtils.getTransientCustomer();
        Account transientMerchantRubAccount = AccountDataUtils.getTransientMerchantRubAccount();
        BDDMockito.given(paymentRepository.findByTransactionIdAndType(transientPayment.getId(), PaymentType.TRANSACTION))
                .willReturn(Mono.just(transientPayment));
        BDDMockito.given(cardService.find(transientPayment.getCardId()))
                .willReturn(Mono.just(transientCard));
        BDDMockito.given(customerService.findById(transientCard.getCustomerId()))
                .willReturn(Mono.just(transientCustomer));
        BDDMockito.given(accountService.find(transientPayment.getAccountId()))
                .willReturn(Mono.just(transientMerchantRubAccount));

        SecurePayment mappedDto = new SecurePayment();
        BDDMockito.given(paymentMapper.mapToSecurePayment(transientPayment))
                .willReturn(mappedDto);
        //when
        StepVerifier.create(topUpService.findInSecureForm(transientPayment.getTransactionId(),PaymentType.TRANSACTION))
                //then
                .expectNext(mappedDto)
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("Test find all payments in non secure form")
    public void givenPaymentTypeStartPeriodEndPeriod_whenFind_thenNonSecurePaymentsListIsReturned(){
        //given
        List<Payment> transientPayments = PaymentDataUtils.getTransientTransactionPaymentsList();
        Card transientCard = CardDataUtils.getTransientCard();
        Customer transientCustomer = CustomerDataUtils.getTransientCustomer();
        Account transientMerchantRubAccount = AccountDataUtils.getTransientMerchantRubAccount();

        BDDMockito.given(paymentRepository.findAllByTypeAndCreatedAtBetween(any(),any(),any()))
                .willReturn(Flux.fromIterable(transientPayments));

        BDDMockito.given(cardService.find(anyString()))
                .willReturn(Mono.just(transientCard));
        BDDMockito.given(customerService.findById(anyString()))
                .willReturn(Mono.just(transientCustomer));
        BDDMockito.given(accountService.find(anyString()))
                .willReturn(Mono.just(transientMerchantRubAccount));

        NonSecurePayment firstMappedDto = new NonSecurePayment();
        NonSecurePayment secondMappedDto = new NonSecurePayment();
        List<NonSecurePayment> nonSecurePayments = List.of(firstMappedDto, secondMappedDto);
        BDDMockito.given(paymentMapper.mapToNonSecurePayment(transientPayments))
                .willReturn(nonSecurePayments);

        //when
        StepVerifier.create(topUpService.findAllInNonSecureForm(any(),any(),any()))
                //then
                .expectNext(nonSecurePayments)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Test find all payments in  secure form")
    public void givenPaymentTypeStartPeriodEndPeriod_whenFind_thenSecurePaymentsListIsReturned(){
        //given
        List<Payment> transientPayments = PaymentDataUtils.getTransientTransactionPaymentsList();
        Card transientCard = CardDataUtils.getTransientCard();
        Customer transientCustomer = CustomerDataUtils.getTransientCustomer();
        Account transientMerchantRubAccount = AccountDataUtils.getTransientMerchantRubAccount();

        BDDMockito.given(paymentRepository.findAllByTypeAndCreatedAtBetween(any(),any(),any()))
                .willReturn(Flux.fromIterable(transientPayments));

        BDDMockito.given(cardService.find(anyString()))
                .willReturn(Mono.just(transientCard));
        BDDMockito.given(customerService.findById(anyString()))
                .willReturn(Mono.just(transientCustomer));
        BDDMockito.given(accountService.find(anyString()))
                .willReturn(Mono.just(transientMerchantRubAccount));

        SecurePayment firstMappedDto = new SecurePayment();
        SecurePayment secondMappedDto = new SecurePayment();
        List<SecurePayment> securePayments = List.of(firstMappedDto, secondMappedDto);
        BDDMockito.given(paymentMapper.mapToSecurePayment(transientPayments))
                .willReturn(securePayments);

        //when
        StepVerifier.create(topUpService.findAllInSecureForm(any(),any(),any()))
                //then
                .expectNext(securePayments)
                .expectComplete()
                .verify();

    }

    @Test
    @DisplayName("Test find all payments")
    public void givenPaymentTypePaymentStatus_whenFind_thenPaymentsAreReturned(){
        //given
        List<Payment> transientPayments = PaymentDataUtils.getTransientTransactionPaymentsList();
        BDDMockito.given(paymentRepository.findAllByTypeAndStatus(any(),any()))
                .willReturn(Flux.fromIterable(transientPayments));
        //when
        StepVerifier.create(topUpService.findAll(any(),any()))
                //then
                .expectNext(transientPayments.toArray(new Payment[2]))
                .expectComplete()
                .verify();
    }
}