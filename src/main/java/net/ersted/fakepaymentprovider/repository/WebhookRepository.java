package net.ersted.fakepaymentprovider.repository;

import net.ersted.fakepaymentprovider.entity.Webhook;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface WebhookRepository extends R2dbcRepository<Webhook, String> {
    @Query("SELECT * FROM webhook w where (w.code not like '200') and w.number_retry <> 5")
    Flux<Webhook> findFailedWebhooks();
}
