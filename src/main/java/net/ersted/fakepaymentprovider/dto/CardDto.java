package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardDto(String id,
                      String cardNumber,
                      LocalDateTime expDate,
                      String cvv,
                      BigDecimal balance,
                      LocalDateTime createdAt,
                      String createdBy,
                      LocalDateTime updatedAt,
                      String updatedBy,
                      CardStatus status,
                      CustomerDto customer,
                      Set<PaymentDto> payments) {
}
