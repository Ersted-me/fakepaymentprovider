package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.AccountRepository;
import net.ersted.fakepaymentprovider.util.AccountDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionalOperator transactionalOperator;

    @InjectMocks
    AccountService accountService;

    @Test
    @DisplayName("Test update account functionality")
    public void givenAccountToSave_whenSave_thenRepositoryIsCalled() {
        //given
        Account updatedAccount = AccountDataUtils.getPersistMerchantRubAccount();
        updatedAccount.setUpdatedBy("field was updated");
        BDDMockito.given(transactionalOperator.transactional(any(Mono.class)))
                .willReturn(Mono.just(updatedAccount));
        BDDMockito.given(accountRepository.save(any(Account.class)))
                .willReturn(Mono.just(updatedAccount));

        //when
        StepVerifier.create(accountService.update(updatedAccount))
                //then
                .expectNextMatches(account -> updatedAccount.getUpdatedBy().equals(account.getUpdatedBy()))
                .verifyComplete();

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("Test find account by id functionality")
    public void givenAccountId_whenFind_thenAccountIsReturned() {
        //given
        BDDMockito.given(accountRepository.findById(anyString()))
                .willReturn(Mono.just(AccountDataUtils.getPersistMerchantRubAccount()));
        //when
        StepVerifier.create(accountService.find(anyString()))

                //then
                .expectNextCount(1)
                .verifyComplete();
        verify(accountRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Test find account by non-existent id functionality")
    public void givenNonExistentAccountId_whenFind_thenExceptionIsThrown() {
        //given
        BDDMockito.given(accountRepository.findById(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(accountService.find("id"))

                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Account was not found")
                        && nfe.getStatus().equals("ACCOUNT_NOT_FOUND"))
                .verify();
        verify(accountRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Test find account by currency and merchantId functionality")
    public void givenCurrencyAndMerchantId_whenFind_thenAccountIsReturned() {
        //given
        Account existAccount = AccountDataUtils.getPersistMerchantRubAccount();
        BDDMockito.given(accountRepository.findByCurrencyAndMerchantId(anyString(), anyString()))
                .willReturn(Mono.just(existAccount));
        //when
        StepVerifier.create(accountService.findByCurrencyAndMerchantId(existAccount.getCurrency(), existAccount.getMerchantId()))

                //then
                .expectNext(existAccount)
                .verifyComplete();
        verify(accountRepository, times(1)).findByCurrencyAndMerchantId(anyString(), anyString());
    }

    @Test
    @DisplayName("Test find account by non-existent currency or merchantId functionality")
    public void givenNonExistentCurrencyAndMerchantId_whenFind_thenExceptionIsThrown() {
        //given
        BDDMockito.given(accountRepository.findByCurrencyAndMerchantId(anyString(), anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.create(accountService.findByCurrencyAndMerchantId(anyString(), anyString()))

                //then
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException nfe
                        && nfe.getMessage().equals("Account was not found")
                        && nfe.getStatus().equals("ACCOUNT_NOT_FOUND"))
                .verify();
        verify(accountRepository, times(1)).findByCurrencyAndMerchantId(anyString(), anyString());
    }
}