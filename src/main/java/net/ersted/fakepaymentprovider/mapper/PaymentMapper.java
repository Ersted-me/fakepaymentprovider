package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.NonSecurePayment;
import net.ersted.fakepaymentprovider.dto.SecurePayment;
import net.ersted.fakepaymentprovider.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    SecurePayment mapToSecurePayment(Payment payment);

    List<SecurePayment> mapToSecurePayment(List<Payment> payments);

    @Mapping(target = "customer", source = "payment.card.customer")
    @Mapping(target = "card", source = "payment.card")
    NonSecurePayment mapToNonSecurePayment(Payment payment);

    List<NonSecurePayment> mapToNonSecurePayment(List<Payment> payments);
}