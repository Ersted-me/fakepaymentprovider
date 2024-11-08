package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.dto.CardDataForCreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.CreateTopUpRq;
import net.ersted.fakepaymentprovider.dto.CustomerForCreateTopUpRq;

import java.math.BigDecimal;

public class PayoutDataUtils {
    public static CreateTopUpRq createPayoutRqBobFirsty() {
        return new CreateTopUpRq("CARD",
                new BigDecimal(10),
                "RUB",
                new CardDataForCreateTopUpRq("0000000000000000", null, null),
                "ru",
                "http://localhost:8080/webhook/transaction",
                new CustomerForCreateTopUpRq("Bob", "Firsty", "RUS"));
    }
}
