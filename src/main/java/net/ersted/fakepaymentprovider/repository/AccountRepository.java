package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<Account, String> {
    Mono<Account> findByCurrencyAndMerchantId(String currency, String merchantId);
//    Mono<Account> selectForUpdate();
}
