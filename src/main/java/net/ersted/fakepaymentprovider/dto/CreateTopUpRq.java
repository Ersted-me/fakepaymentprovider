package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateTopUpRq(String paymentMethod,
                            BigDecimal amount,
                            String currency,
                            CardDataForCreateTopUpRq cardData,
                            String language,
                            String notificationUrl,
                            CustomerForCreateTopUpRq customer) {
}