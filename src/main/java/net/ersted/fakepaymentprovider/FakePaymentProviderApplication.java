package net.ersted.fakepaymentprovider;

import net.ersted.fakepaymentprovider.entity.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FakePaymentProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakePaymentProviderApplication.class, args);
    }

}
