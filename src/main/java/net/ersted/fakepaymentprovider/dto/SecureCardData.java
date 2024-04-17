package net.ersted.fakepaymentprovider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SecureCardData extends NonSecureCardData {
    public String getCardNumber() {
        char[] ca = cardNumber.toCharArray();
        Arrays.fill(ca, 4, cardNumber.length()-4, '*');
        return new String(ca);
    }
}