package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Card;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CardRepository extends R2dbcRepository<Card, String> {
}