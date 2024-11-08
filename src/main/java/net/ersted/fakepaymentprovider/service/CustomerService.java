package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import net.ersted.fakepaymentprovider.entity.Customer;
import net.ersted.fakepaymentprovider.enums.CustomerStatus;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static reactor.core.publisher.Operators.as;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final TransactionalOperator transactionalOperator;

    public Mono<Customer> findById(String customerId) {
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new NotFoundException("CUSTOMER_NOT_FOUND", "Customer was not found")));
    }

    public Mono<Customer> find(String firstName, String lastName, String country) {
        return customerRepository.findByFirstNameAndLastNameAndCountry(firstName, lastName, country)
                .switchIfEmpty(Mono.error(new NotFoundException("CUSTOMER_NOT_FOUND", "Customer was not found")));
    }

    public Mono<Customer> save(String firstName, String lastName, String country) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .country(country)
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(CustomerStatus.ACTIVE)
                .build();
        return customerRepository.save(customer)
                .as(transactionalOperator::transactional);
    }
}