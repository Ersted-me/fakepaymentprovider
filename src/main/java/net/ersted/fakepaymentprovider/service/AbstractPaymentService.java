package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.dto.*;
import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;
import net.ersted.fakepaymentprovider.exception.PaymentException;
import net.ersted.fakepaymentprovider.mapper.PaymentMapper;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPaymentService implements PaymentService {

    protected final String FAILED_STATUS = "FAILED";
    protected final String PAYMENT_METHOD_NOT_ALLOWED_FAILED_MESSAGE = "PAYMENT_METHOD_NOT_ALLOWED";
    protected final String WITHDRAWAL_OPERATION_FAILED_MESSAGE = "WITHDRAWAL_OPERATION_IMPOSSIBLE";
    protected final String TRANSACTION_OPERATION_IMPOSSIBLE = "TRANSACTION_OPERATION_IMPOSSIBLE";
    protected final Long NUM_RETRIES_TRANSACTIONAL = 3L;

    protected final PaymentRepository paymentRepository;
    protected final PaymentMapper paymentMapper;
    protected final AccountService accountService;
    protected final CardService cardService;
    protected final CustomerService customerService;
    protected final TransactionalOperator transactionalOperator;
    protected final WebhookService webhookService;


    public Mono<CreatePaymentRs> create(CreateTopUpRq dto, PaymentType type, String merchantId) {
        Mono<Account> accountMono = accountService.findByCurrencyAndMerchantId(dto.currency(), merchantId)
                .doOnError(e -> {
                    log.error("the account could not be found by currency: {}, merchantId: {}. Exception: {}", dto.currency(), merchantId, e.getStackTrace());
                    throw new PaymentException(FAILED_STATUS, PAYMENT_METHOD_NOT_ALLOWED_FAILED_MESSAGE);
                });

        Mono<Card> cardMono = customerService.find(dto.customer().firstName(), dto.customer().lastName(), dto.customer().country())
                .onErrorResume(error -> customerService.save(dto.customer().firstName(), dto.customer().lastName(), dto.customer().country()))
                .flatMap(customer -> cardService.find(dto.cardData().cardNumber(), dto.cardData().expDate(), dto.cardData().cvv())
                        .onErrorResume(error -> cardService.save(dto.cardData().cardNumber(), dto.cardData().expDate(), dto.cardData().cvv(), customer.getId())));

        return Mono.zip(accountMono, cardMono).flatMap(tuples -> {
                    Account account = tuples.getT1();
                    Card card = tuples.getT2();
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    Payment payment = Payment.builder()
                            .language(dto.language())
                            .notificationUrl(dto.notificationUrl())
                            .message("")
                            .type(type)
                            .paymentMethod(dto.paymentMethod())
                            .currency(dto.currency())
                            .amount(dto.amount())
                            .cardId(card.getId())
                            .card(card)
                            .accountId(account.getId())
                            .account(account)
                            .createdAt(currentDateTime)
                            .createdBy("")
                            .updatedAt(currentDateTime)
                            .updatedBy("")
                            .status(PaymentStatus.IN_PROCESS)
                            .build();
                    return paymentRepository.save(payment);
                })
                .flatMap(this::withdrawalAmountFromBalance)
                .as(transactionalOperator::transactional)
                .retry(NUM_RETRIES_TRANSACTIONAL)
                .map(payment -> new CreatePaymentRs(payment.getTransactionId(), payment.getStatus(), "OK"));
    }

    @Override
    public Mono<Payment> approve(Payment payment) {
        return transactionAmountToBalance(payment)
                .flatMap(p -> {
                    p.setStatus(PaymentStatus.APPROVED);
                    return paymentRepository.save(p);
                })
                .doOnSuccess(p -> log.debug("Payment has been success done: {}", p))
                .doOnError(transactionExc -> log.debug("Payment has been failed done: {}. Exception: {}", payment, transactionExc.getMessage()))
                .onErrorResume(throwable ->
                        balanceRollback(payment)
                                .flatMap(p -> {
                                    p.setStatus(PaymentStatus.FAILED);
                                    return paymentRepository.save(p);
                                }))
                .doOnSuccess(p -> log.debug("Payment rollback has been success done: {}", p))
                .doOnError(rollbackExc -> log.debug("Payment rollback has been failed done: {}. Exception: {}", payment, rollbackExc.getMessage()))
                .as(transactionalOperator::transactional)
                .retry(NUM_RETRIES_TRANSACTIONAL)
                .onErrorResume(throwable -> Mono.just(payment)
                        .doOnNext(p -> log.error("Payment hasn't been processed: {}. Cause: {}", p, throwable.getMessage())))
                .flatMap(p -> Mono.fromSupplier(() -> paymentMapper.mapToSecurePayment(p))
                        .flatMap(webhookService::sendWebhook)
                        .thenReturn(p));
    }

    protected abstract Mono<Payment> withdrawalAmountFromBalance(Payment payment);

    protected abstract Mono<Payment> transactionAmountToBalance(Payment payment);

    protected abstract Mono<Payment> balanceRollback(Payment payment);

    @Override
    public Mono<NonSecurePayment> findInNonSecureForm(String transactionId, PaymentType type) {
        return findWithTransients(transactionId, type)
                .map(paymentMapper::mapToNonSecurePayment);
    }

    @Override
    public Mono<SecurePayment> findInSecureForm(String transactionId, PaymentType type) {
        return findWithTransients(transactionId, type)
                .map(paymentMapper::mapToSecurePayment);
    }

    private Mono<Payment> findWithTransients(String transactionId, PaymentType type) {
        return paymentRepository.findByTransactionIdAndType(transactionId, type)
                .flatMap(this::loadTransientObjects);
    }

    @Override
    public Mono<List<NonSecurePayment>> findAllInNonSecureForm(PaymentType paymentType, Instant start, Instant end) {
        return findAllWithTransients(paymentType, start, end)
                .map(paymentMapper::mapToNonSecurePayment);
    }

    @Override
    public Mono<List<SecurePayment>> findAllInSecureForm(PaymentType paymentType, Instant start, Instant end) {
        return findAllWithTransients(paymentType, start, end)
                .map(paymentMapper::mapToSecurePayment);
    }

    private Mono<List<Payment>> findAllWithTransients(PaymentType paymentType, Instant start, Instant end) {
        return paymentRepository.findAllByTypeAndCreatedAtBetween(paymentType, start, end)
                .flatMap(this::loadTransientObjects)
                .collectList();
    }

    @Override
    public Flux<Payment> findAll(PaymentType paymentType, PaymentStatus status) {
        return paymentRepository.findAllByTypeAndStatus(paymentType, status);
    }

    protected Mono<Payment> loadTransientObjects(Payment payment) {
        Mono<Card> cardMono = cardService.find(payment.getCardId())
                .flatMap(card -> customerService.findById(card.getCustomerId())
                        .map(customer -> {
                            card.setCustomer(customer);
                            return card;
                        }));
        Mono<Account> accountMono = accountService.find(payment.getAccountId());
        return Mono.zip(cardMono, accountMono)
                .flatMap(tuples -> {
                    Card card = tuples.getT1();
                    Account account = tuples.getT2();
                    payment.setCard(card);
                    payment.setAccount(account);
                    return Mono.just(payment);
                });
    }
}