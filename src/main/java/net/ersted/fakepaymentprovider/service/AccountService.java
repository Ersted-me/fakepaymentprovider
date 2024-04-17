package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Account> update(Account account) {
        return accountRepository.save(account)
               .as(transactionalOperator::transactional);
    }

    public Mono<Account> find(String accountId){
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new NotFoundException("ACCOUNT_NOT_FOUND", "Account was not found")));
    }

    public Mono<Account> findByCurrencyAndMerchantId(String currency, String merchantId) {
        return accountRepository.findByCurrencyAndMerchantId(currency, merchantId)
                .switchIfEmpty(Mono.error(new NotFoundException("ACCOUNT_NOT_FOUND", "Account was not found")));
    }
}