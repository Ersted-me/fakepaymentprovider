package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.entity.Account;
import net.ersted.fakepaymentprovider.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDataUtils {

    public static Account getTransientMerchantRubAccount() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Account.builder()
                .id(null)
                .currency("RUB")
                .balance(BigDecimal.valueOf(1000))
                .payments(null)
                .merchantId("MERCHANT")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(AccountStatus.ACTIVE)
                .build();
    }

    public static Account getPersistMerchantRubAccount() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Account.builder()
                .id("id")
                .currency("RUB")
                .balance(BigDecimal.valueOf(1000))
                .payments(null)
                .merchantId("MERCHANT")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(AccountStatus.ACTIVE)
                .build();
    }
}