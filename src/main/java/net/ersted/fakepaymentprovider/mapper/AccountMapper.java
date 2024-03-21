package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.AccountDto;
import net.ersted.fakepaymentprovider.entity.Account;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account map(AccountDto dto);

    @InheritInverseConfiguration
    AccountDto map(Account entity);
}
