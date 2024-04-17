package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Merchant;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface MerchantRepository extends R2dbcRepository<Merchant, String> {
    //@Query("SELECT m FROM merchant m where m.merchant_id=?1 and status = 'ACTIVE'")
    Mono<Merchant> findActiveByMerchantId(String merchantId);
}
