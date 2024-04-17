package net.ersted.fakepaymentprovider.util;

import net.ersted.fakepaymentprovider.entity.Customer;
import net.ersted.fakepaymentprovider.enums.CustomerStatus;

import java.time.LocalDateTime;

public class CustomerDataUtils {

    public static Customer getTransientCustomer() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Customer.builder()
                .id("id")
                .country("country")
                .lastName("lastName")
                .firstName("firstName")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    public static Customer getPersistCustomer() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return Customer.builder()
                .id(null)
                .country("country")
                .lastName("lastName")
                .firstName("firstName")
                .createdAt(currentDateTime)
                .createdBy("auto")
                .updatedAt(currentDateTime)
                .updatedBy("auto")
                .status(CustomerStatus.ACTIVE)
                .build();
    }

}