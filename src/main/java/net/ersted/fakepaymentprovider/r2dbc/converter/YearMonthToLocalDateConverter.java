package net.ersted.fakepaymentprovider.r2dbc.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@WritingConverter
public class YearMonthToLocalDateConverter implements Converter<YearMonth, LocalDateTime> {
    @Override
    public LocalDateTime convert(YearMonth source) {
        return source.atDay(1).atStartOfDay();
    }
}
