package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.CustomerDto;
import net.ersted.fakepaymentprovider.entity.Customer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer map(CustomerDto dto);

    @InheritInverseConfiguration
    CustomerDto map(Customer entity);
}
