package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetPaymentDetailsRs(
        String paymentMethod,
        String amount,
        String currency,
        String transactionId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String notificationUrl,
        NonSecureCardData card,
        String language,
        CustomerGetTopUpDetailsRs customer,
        PaymentStatus status,
        String message) {
}
