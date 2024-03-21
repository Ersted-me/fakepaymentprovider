package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.WebhookDto;
import net.ersted.fakepaymentprovider.entity.Webhook;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebhookMapper {
    Webhook map(WebhookDto dto);

    @InheritInverseConfiguration
    WebhookDto map(Webhook entity);
}
