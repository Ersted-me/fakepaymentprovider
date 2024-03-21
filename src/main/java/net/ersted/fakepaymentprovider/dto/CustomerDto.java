package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.ersted.fakepaymentprovider.enums.CustomerStatus;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CustomerDto(String id,
                          String firstName,
                          String lastName,
                          String country,
                          LocalDateTime createdAt,
                          String createdBy,
                          LocalDateTime updatedAt,
                          String updatedBy,
                          CustomerStatus status,
                          List<CardDto> cards) {
}
