package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Payment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentRepository extends R2dbcRepository<Payment, String> {
}
