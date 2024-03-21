package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.entity.Payment;
import net.ersted.fakepaymentprovider.enums.WebhookStatus;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WebhookDto(String id,
                         String url,
                         String request,
                         String response,
                         String code,
                         String numberRetry,
                         LocalDateTime lastRetryAt,
                         LocalDateTime createdAt,
                         String createdBy,
                         LocalDateTime updatedAt,
                         String updatedBy,
                         WebhookStatus status,
                         Set<Payment> payments) {
}