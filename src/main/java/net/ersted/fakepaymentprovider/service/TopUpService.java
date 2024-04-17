package net.ersted.fakepaymentprovider.service;

import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.exception.PaymentException;
import net.ersted.fakepaymentprovider.mapper.PaymentMapper;
import net.ersted.fakepaymentprovider.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Service("topUpService")
public class TopUpService extends AbstractPaymentService {

    @Autowired
    public TopUpService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, AccountService accountService, CardService cardService, CustomerService customerService, TransactionalOperator transactionalOperator, WebhookService webhookService) {
        super(paymentRepository, paymentMapper, accountService, cardService, customerService, transactionalOperator, webhookService);
    }

    @Override
    protected Mono<Payment> withdrawalAmountFromBalance(Payment payment) {
        Card card = payment.getCard();
        if (card.getBalance().compareTo(payment.getAmount()) >= 0) {
            card.setBalance(card.getBalance().subtract(payment.getAmount()));
        } else {
            throw new PaymentException(FAILED_STATUS, WITHDRAWAL_OPERATION_FAILED_MESSAGE);
        }
        return cardService.save(card)
                .thenReturn(payment);
    }

    @Override
    protected Mono<Payment> transactionAmountToBalance(Payment payment) {
        return accountService.find(payment.getAccountId())
                .switchIfEmpty(Mono.error(new PaymentException(FAILED_STATUS, TRANSACTION_OPERATION_IMPOSSIBLE)))
                .doOnSuccess(account -> log.debug("Account was found: {}", account))
                .doOnError(throwable -> log.debug("Account wasn't found: {}", throwable.getMessage()))
                .flatMap(account -> {
                    if (Math.random() >= 0.7) {
                        return Mono.error(new RuntimeException("Transaction failed"));
                    }
                    account.setBalance(account.getBalance().add(payment.getAmount()));
                    return accountService.update(account);
                })
                .doOnSuccess(account -> log.debug("Account balance has been topped up : {}", account))
                .doOnError(throwable -> log.debug("Account balance has not been topped up: {}", throwable.getMessage()))
                .map(account -> payment);
    }

    @Override
    protected Mono<Payment> balanceRollback(Payment payment) {
        return cardService.find(payment.getCardId())
                .switchIfEmpty(Mono.error(new PaymentException(FAILED_STATUS, TRANSACTION_OPERATION_IMPOSSIBLE)))
                .doOnSuccess(card -> log.debug("Card was found: {}", card))
                .doOnError(throwable -> log.debug("Card wasn't found: {}", throwable.getMessage()))
                .flatMap(card -> {
                    card.setBalance(card.getBalance().add(payment.getAmount()));
                    return cardService.save(card);
                })
                .doOnSuccess(card -> log.debug("Card balance has been topped up : {}", card))
                .doOnError(throwable -> log.debug("Card balance has not been topped up: {}", throwable.getMessage()))
                .map(card -> payment);
    }
}