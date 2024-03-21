package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Merchant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MerchantRepository extends R2dbcRepository<Merchant, String> {
}
