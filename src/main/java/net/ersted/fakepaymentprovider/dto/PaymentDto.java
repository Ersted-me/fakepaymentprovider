package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentDto(String id,
                         String transactionId,
                         String language,
                         String notificationUrl,
                         String message,
                         String type,
                         String currency,
                         LocalDateTime createdAt,
                         String createdBy,
                         LocalDateTime updatedAt,
                         String updatedBy,
                         PaymentStatus status,
                         CardDto card,
                         AccountDto account) {

}
