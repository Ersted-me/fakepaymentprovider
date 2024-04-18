package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.dto.NonSecurePayment;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentDataUtils {
    public static Payment getTransientTransactionPayment() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Payment.builder()
                .transactionId("transactionId")
                .language("RU")
                .notificationUrl("https://notification-url.ru")
                .message("some message")
                .type(PaymentType.TRANSACTION)
                .paymentMethod("CARD")
                .currency("RUB")
                .amount(new BigDecimal(1000))
                .cardId("cardId")
                .accountId("accountId")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(PaymentStatus.IN_PROCESS)
                .build();
    }

    public static List<Payment> getTransientTransactionPaymentsList() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Payment firstPayment = Payment.builder()
                .transactionId("firstPaymentId")
                .language("RU")
                .notificationUrl("https://notification-url.ru")
                .message("some message")
                .type(PaymentType.TRANSACTION)
                .paymentMethod("CARD")
                .currency("RUB")
                .amount(new BigDecimal(1000))
                .cardId("cardId")
                .accountId("accountId")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(PaymentStatus.IN_PROCESS)
                .build();

        Payment secondPayment = Payment.builder()
                .transactionId("secondPaymentId")
                .language("RU")
                .notificationUrl("https://notification-url.ru")
                .message("some message")
                .type(PaymentType.TRANSACTION)
                .paymentMethod("CARD")
                .currency("RUB")
                .amount(new BigDecimal(1000))
                .cardId("cardId")
                .accountId("accountId")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(PaymentStatus.IN_PROCESS)
                .build();
        return List.of(firstPayment, secondPayment);
    }

}
