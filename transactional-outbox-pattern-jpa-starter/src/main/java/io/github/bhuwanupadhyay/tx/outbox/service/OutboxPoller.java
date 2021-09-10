package io.github.bhuwanupadhyay.tx.outbox.service;

import io.github.bhuwanupadhyay.tx.outbox.config.OutboxProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;


@RequiredArgsConstructor
@Slf4j
@Component
public class OutboxPoller {

    private final PollerInfo info = new PollerInfo();
    private final OutboxRelay outboxRelay;
    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "${boot.outbox.polling-interval-in-milliseconds}")
    public void poller() {

        if (info.hasNextRun()) {
            return;
        }

        log.info("Polling outboxes.");

        info.reset();

        final Instant start = Instant.now();

        while (info.hasNextRun()) {
            try {
                this.outboxRelay.checkout(info);
            } catch (Exception e) {
                info.offNextRun();
                log.error("Error occurred on outbox relay.", e);
            }
        }

        info.offNextRun();

        final Instant finish = Instant.now();

        final long timeElapsed = Duration.between(start, finish).toMillis();

        if (info.hasAnySent()) {
            log.info(
                    "Successfully sent {} outboxes in {} ms with {} max events per polling.",
                    info.getCount(),
                    timeElapsed,
                    properties.getMaxEventsPerPolling()
            );
            double avg = (double) timeElapsed / (double) info.getCount();
            log.info("Outbox relay rate [avg={}ms per outbox].", String.format("%.2f", avg));
        } else {
            log.info("No outboxes found.");
        }
    }

}
