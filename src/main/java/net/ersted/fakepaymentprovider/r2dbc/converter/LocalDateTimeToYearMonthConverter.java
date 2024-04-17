package net.ersted.fakepaymentprovider.r2dbc.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;
import java.time.YearMonth;
@ReadingConverter
public class LocalDateTimeToYearMonthConverter implements Converter<LocalDateTime, YearMonth> {
    @Override
    public YearMonth convert(LocalDateTime source) {
        return YearMonth.of(source.getYear(), source.getMonth());
    }
}
