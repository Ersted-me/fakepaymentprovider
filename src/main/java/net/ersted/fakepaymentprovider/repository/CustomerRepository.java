package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, String> {
}
