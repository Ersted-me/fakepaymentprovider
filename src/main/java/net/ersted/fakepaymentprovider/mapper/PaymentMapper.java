package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.PaymentDto;
import net.ersted.fakepaymentprovider.entity.Payment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment map(PaymentDto dto);

    @InheritInverseConfiguration
    PaymentDto map(Payment entity);
}
