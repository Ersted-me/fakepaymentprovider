package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NonSecurePayment {
    private String paymentMethod;
    private String amount;
    private String currency;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notificationUrl;
    @JsonProperty("card_data")
    private NonSecureCardData card;
    private String language;
    private CustomerGetTopUpDetailsRs customer;
    private PaymentStatus status;
    private String message;
}