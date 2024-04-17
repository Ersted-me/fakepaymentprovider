package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<Customer, String> {
    Mono<Customer> findByFirstNameAndLastNameAndCountry(String firstName, String lastName, String country);
}
