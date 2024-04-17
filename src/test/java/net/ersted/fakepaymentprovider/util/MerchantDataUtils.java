package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.entity.Merchant;
import net.ersted.fakepaymentprovider.enums.MerchantStatus;

import java.time.LocalDateTime;

public class MerchantDataUtils {
    public static Merchant getActivePersistMerchant(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Merchant.builder()
                .merchantId("MERCHANT")
                .secretKey("secretKey")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(MerchantStatus.ACTIVE)
                .build();
    }
}
