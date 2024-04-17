package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import net.ersted.fakepaymentprovider.enums.PaymentStatus;

import java.math.BigDecimal;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String transactionId;
    String language;
    String notificationUrl;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String message;
    String paymentMethod;
    BigDecimal amount;
    String currency;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    PaymentStatus status;
    @JsonProperty("card_data")
    CardDto card;
    CustomerDto customer;
}
