package net.ersted.fakepaymentprovider.service;

import lombok.RequiredArgsConstructor;
import net.ersted.fakepaymentprovider.entity.Merchant;
import net.ersted.fakepaymentprovider.exception.NotFoundException;
import net.ersted.fakepaymentprovider.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;

    public Mono<Merchant> getActiveMerchantByMerchantId(String merchantId) {
        return merchantRepository.findActiveByMerchantId(merchantId)
                .switchIfEmpty(Mono.error(new NotFoundException("MERCHANT_NOT_FOUND", "Merchant was not found")));
    }
}