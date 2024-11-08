package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.dto.CardDataForCreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.CustomerForCreateTopUpRq;

import java.math.BigDecimal;
import java.time.YearMonth;

public class TransactionDataUtils {
    public static CreateTopUpRq createTopUpRqBobFirsty(){
        return new CreateTopUpRq("CARD",
                new BigDecimal(10),
                "RUB",
                new CardDataForCreateTopUpRq("0000000000000000", YearMonth.of(1,1), "001"),
                "ru",
                "http://localhost:8080/webhook/transaction",
                new CustomerForCreateTopUpRq("Bob", "Firsty", "RUS"));
    }

    public static CreateTopUpRq createTopUpRqMamyMomy(){
        return new CreateTopUpRq("CARD",
                new BigDecimal(10000),
                "RUB",
                new CardDataForCreateTopUpRq("0000000000000000", YearMonth.of(1,1), "001"),
                "ru",
                "http://localhost:8080/webhook/transaction",
                new CustomerForCreateTopUpRq("Mamy", "Momy", "RUS"));
    }
}
