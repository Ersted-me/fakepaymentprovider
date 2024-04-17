package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.SecurePayment;
import net.ersted.fakepaymentprovider.dto.WebhookRq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebhookMapper {
    WebhookRq mapToRq(SecurePayment securePayment);
}