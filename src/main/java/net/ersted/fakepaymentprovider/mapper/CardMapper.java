package net.ersted.fakepaymentprovider.mapper;

import net.ersted.fakepaymentprovider.dto.CardDto;
import net.ersted.fakepaymentprovider.entity.Card;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card map(CardDto dto);

    @InheritInverseConfiguration
    CardDto map(Card entity);
}
