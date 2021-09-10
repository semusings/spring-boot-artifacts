package io.github.bhuwanupadhyay.tx.outbox.service;

import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxEvent;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringOutboxPublisher implements OutboxPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void fire(OutboxEvent outboxEvent) {
        applicationEventPublisher.publishEvent(outboxEvent);
    }
}
