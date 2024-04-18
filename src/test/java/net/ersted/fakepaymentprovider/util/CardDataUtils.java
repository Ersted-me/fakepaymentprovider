package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.entity.Card;
import net.ersted.fakepaymentprovider.enums.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardDataUtils {
    public static Card getTransientCard() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Card.builder()
                .id("id")
                .cardNumber("1234567890123456")
                .cvv(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0).toString())
                .balance(new BigDecimal(1000))
                .customerId("customerId")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(CardStatus.ACTIVE)
                .build();
    }


    public static Card getPersistCard() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Card.builder()
                .id(null)
                .cardNumber("1234567890123456")
                .cvv(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0).toString())
                .balance(new BigDecimal(1000))
                .customerId("customerId")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(CardStatus.ACTIVE)
                .build();
    }
}