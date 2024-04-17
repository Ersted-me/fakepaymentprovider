package net.ersted.fakepaymentprovider.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import net.ersted.fakepaymentprovider.r2dbc.converter.LocalDateTimeToYearMonthConverter;
import net.ersted.fakepaymentprovider.r2dbc.converter.YearMonthToLocalDateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {
    private final ConnectionFactory connectionFactory;

    @Bean
    public LocalDateTimeToYearMonthConverter localDateTimeToYearMonthConverter(){
        return new LocalDateTimeToYearMonthConverter();
    }

    @Bean
    public YearMonthToLocalDateConverter yearMonthToLocalDateConverter(){
        return new YearMonthToLocalDateConverter();
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(localDateTimeToYearMonthConverter(),yearMonthToLocalDateConverter());
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager manager){
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
//        definition.setTimeout(15);
        return TransactionalOperator.create(manager, definition);
    }

}