package net.ersted.fakepaymentprovider.service;

import net.ersted.fakepaymentprovider.entity.Merchant;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.MerchantRepository;
import net.ersted.fakepaymentprovider.util.MerchantDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {
    @Mock
    MerchantRepository merchantRepository;

    @InjectMocks
    MerchantService merchantService;

    @Test
    @DisplayName("Test find active merchant by id functionality")
    public void givenActivePersistMerchant_whenFindById_thenMerchantIsReturned() {
        //given
        Merchant persistMerchant = MerchantDataUtils.getActivePersistMerchant();
        BDDMockito.given(merchantRepository.findActiveByMerchantId(any()))
                .willReturn(Mono.just(persistMerchant));
        //when
        StepVerifier.FirstStep<Merchant> stepVerifier = StepVerifier.create(merchantService.getActiveMerchantByMerchantId(any()));

        //then
        stepVerifier.expectNext(persistMerchant)
                .verifyComplete();

        verify(merchantRepository, times(1))
                .findActiveByMerchantId(any());

    }

    @Test
    @DisplayName("Test find merchant by non-existent id functionality")
    public void givenNonExistentMerchantId_whenFindById_thenExceptionIsThrown() {
        //given
        BDDMockito.given(merchantRepository.findActiveByMerchantId(anyString()))
                .willReturn(Mono.empty());
        //when
        StepVerifier.FirstStep<Merchant> stepVerifier = StepVerifier.create(merchantService.getActiveMerchantByMerchantId(anyString()));

        //then
        stepVerifier.expectError(NotFoundException.class).verify();
        verify(merchantRepository, times(1)).findActiveByMerchantId(anyString());


    }
}