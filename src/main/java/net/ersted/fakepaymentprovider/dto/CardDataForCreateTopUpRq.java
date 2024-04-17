package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.YearMonth;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CardDataForCreateTopUpRq(String cardNumber,
                                       @JsonFormat(pattern = "MM/yy") YearMonth expDate,
                                       String cvv) {
}