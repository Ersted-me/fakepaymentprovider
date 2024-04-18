package net.ersted.fakepaymentprovider.job;

import lombok.RequiredArgsConstructor;
import net.ersted.fakepaymentprovider.service.WebhookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class WebhookProcess {
    private final WebhookService webhookService;

    @Scheduled(cron = "*/30 * * * * *")
    public Mono<Void> failedWebhookProcess() {

        return webhookService.findAllFailedWebhooks()
                .flatMap(webhookService::processFailedWebhook)
                .subscribeOn(Schedulers.newBoundedElastic(3, 500, "Scheduler-elastic-3-500"))
                .then();
    }
}
