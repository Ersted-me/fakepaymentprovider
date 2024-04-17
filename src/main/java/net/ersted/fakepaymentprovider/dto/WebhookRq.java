package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;
import net.ersted.fakepaymentprovider.enums.PaymentType;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WebhookRq {
    private String paymentMethod;
    private String amount;
    private String currency;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PaymentType type;
    @JsonProperty("card_data")
    private SecureCardData card;
    private String language;
    private SecureCustomer customer;
    private PaymentStatus status;
    private String message;
}