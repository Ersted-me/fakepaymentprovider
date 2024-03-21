package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccountDto(String id,
                         String currency,
                         BigDecimal balance,
                         LocalDateTime createdAt,
                         String createdBy,
                         LocalDateTime updatedAt,
                         String updatedBy,
                         AccountStatus status,
                         MerchantDto merchant,
                         Set<PaymentDto> payments) {
}
