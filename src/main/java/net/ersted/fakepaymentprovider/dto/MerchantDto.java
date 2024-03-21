package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.MerchantStatus;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MerchantDto(String merchantId,
                          String secretKey,
                          LocalDateTime createdAt,
                          String createdBy,
                          LocalDateTime updatedAt,
                          String updatedBy,
                          MerchantStatus status,
                          Set<AccountDto> accounts) {

}
