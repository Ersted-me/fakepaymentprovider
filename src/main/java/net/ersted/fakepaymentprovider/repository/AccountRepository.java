package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AccountRepository extends R2dbcRepository<Account, String> {
}
