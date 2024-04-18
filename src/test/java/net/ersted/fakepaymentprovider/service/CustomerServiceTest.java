package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.entity.Customer;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.CustomerRepository;
import net.ersted.fakepaymentprovider.util.CustomerDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    TransactionalOperator transactionalOperator;

    @InjectMocks
    CustomerService customerService;

    @Test
    @DisplayName("Test find account by id functionality")
    public void givenCustomerId_whenFindById_thenCustomerIsReturned() {
        //given
        BDDMockito.given(customerRepository.findById(anyString()))
                .willReturn(Mono.just(CustomerDataUtils.getPersistCustomer()));
        //when
        StepVerifier.create(customerService.findById(anyString()))

                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(customerRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Test find account by non-existent id functionality")
    public void givenNonExistentCustomerId_whenFindById_thenExceptionIsThrown() {
        //given
        BDDMockito.given(customerRepository.findById(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(customerService.findById(anyString()))

                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Customer was not found")
                        && nfe.getStatus().equals("CUSTOMER_NOT_FOUND"))
                .verify();
        verify(customerRepository, times(1)).findById(anyString());
    }


    @Test
    @DisplayName("Test find account by non-existent lastName, firstname or country functionality")
    public void givenFirstNameOrLastNameOrCountry_whenFind_thenCustomerIsReturned() {
        //given
        BDDMockito.given(customerRepository.findByFirstNameAndLastNameAndCountry(anyString(), anyString(), anyString()))
                .willReturn(Mono.just(CustomerDataUtils.getPersistCustomer()));
        //when
        StepVerifier.create(customerService.find(anyString(), anyString(), anyString()))

                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(customerRepository, times(1))
                .findByFirstNameAndLastNameAndCountry(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Test find account by non-existent lastName, firstname or country functionality")
    public void givenNonExistentFirstNameOrLastNameOrCountry_whenFind_thenExceptionIsThrown() {
        //given
        BDDMockito.given(customerRepository.findByFirstNameAndLastNameAndCountry(anyString(), anyString(), anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(customerService.find(anyString(), anyString(), anyString()))

                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Customer was not found")
                        && nfe.getStatus().equals("CUSTOMER_NOT_FOUND"))
                .verify();
        verify(customerRepository, times(1))
                .findByFirstNameAndLastNameAndCountry(anyString(), anyString(), anyString());
    }

//    @Test
//    @DisplayName("Test save customer functionality")
//    public void givenPersistCustomer_whenSave_thenCustomerIsReturned() {
//        //given
//        Customer transientCustomer = CustomerDataUtils.getTransientCustomer();
//
//        BDDMockito.given(customerRepository.save(any(Customer.class)))
//                .willReturn(Mono.just(transientCustomer));
//
//        BDDMockito.given(transactionalOperator.transactional(any(Mono.class)))
//                .willReturn(Mono.just(transientCustomer));
//
//        //when
//        StepVerifier.create(customerService.save(transientCustomer.getFirstName(), transientCustomer.getLastName(), transientCustomer.getCountry()))
//                //then
//                .expectNext(transientCustomer)
//                .expectComplete()
//                .verify();
//        verify(customerRepository, times(1)).save(any(Customer.class));
//        verifyNoMoreInteractions(customerRepository);
//    }
}