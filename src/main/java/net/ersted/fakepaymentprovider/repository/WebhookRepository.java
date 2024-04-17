package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Webhook;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface WebhookRepository extends R2dbcRepository<Webhook, String> {
}
