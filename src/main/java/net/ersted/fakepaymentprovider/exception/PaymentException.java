package net.ersted.fakepaymentprovider.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentException extends RuntimeException {
    private String status;
    private String message;
}
