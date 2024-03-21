package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.MerchantDto;
import net.ersted.fakepaymentprovider.entity.Merchant;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantMapper {
    Merchant map(MerchantDto dto);

    @InheritInverseConfiguration
    MerchantDto map(Merchant entity);
}
