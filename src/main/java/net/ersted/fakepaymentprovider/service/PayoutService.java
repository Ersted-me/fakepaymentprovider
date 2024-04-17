package net.ersted.fakepaymentprovider.service;

import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.exception.PaymentException;
import net.ersted.fakepaymentprovider.mapper.PaymentMapper;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Service("payoutService")
public class PayoutService extends AbstractPaymentService {
    @Autowired
    public PayoutService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, AccountService accountService, CardService cardService, CustomerService customerService, TransactionalOperator transactionalOperator, WebhookService webhookService) {
        super(paymentRepository, paymentMapper, accountService, cardService, customerService, transactionalOperator, webhookService);
    }

    @Override
    protected Mono<Payment> withdrawalAmountFromBalance(Payment payment) {
        Account account = payment.getAccount();
        if (account.getBalance().compareTo(payment.getAmount()) >= 0) {
            account.setBalance(account.getBalance().subtract(payment.getAmount()));
        } else {
            throw new PaymentException(FAILED_STATUS, WITHDRAWAL_OPERATION_FAILED_MESSAGE);
        }
        return accountService.update(account)
                .thenReturn(payment);
    }

    @Override
    protected Mono<Payment> transactionAmountToBalance(Payment payment) {
        return cardService.find(payment.getCardId())
                .switchIfEmpty(Mono.error(new PaymentException(FAILED_STATUS, TRANSACTION_OPERATION_IMPOSSIBLE)))
                .doOnSuccess(card -> log.debug("Card was found: {}", card))
                .doOnError(throwable -> log.debug("Card wasn't found: {}", throwable.getMessage()))
                .flatMap(card -> {
                    if (Math.random() >= 0.7) {
                        return Mono.error(new RuntimeException("Transaction failed"));
                    }
                    card.setBalance(card.getBalance().add(payment.getAmount()));
                    return cardService.save(card);
                })
                .doOnSuccess(card -> log.debug("Card balance has been topped up : {}", card))
                .doOnError(throwable -> log.debug("Card balance has not been topped up: {}", throwable.getMessage()))
                .map(card -> payment);
    }

    @Override
    protected Mono<Payment> balanceRollback(Payment payment) {
        return accountService.find(payment.getAccountId())
                .switchIfEmpty(Mono.error(new PaymentException(FAILED_STATUS, TRANSACTION_OPERATION_IMPOSSIBLE)))
                .doOnSuccess(account -> log.debug("Account was found: {}", account))
                .doOnError(throwable -> log.debug("Account wasn't found: {}", throwable.getMessage()))
                .flatMap(account -> {
                    account.setBalance(account.getBalance().add(payment.getAmount()));
                    return accountService.update(account);
                })
                .doOnSuccess(account -> log.debug("Account balance has been topped up : {}", account))
                .doOnError(throwable -> log.debug("Account balance has not been topped up: {}", throwable.getMessage()))
                .map(account -> payment);
    }
}