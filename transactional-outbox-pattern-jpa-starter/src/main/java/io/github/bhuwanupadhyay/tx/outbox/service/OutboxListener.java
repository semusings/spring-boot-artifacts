package io.github.bhuwanupadhyay.tx.outbox.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bhuwanupadhyay.tx.outbox.domain.Outbox;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxEvent;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;


@RequiredArgsConstructor
@Component
@Slf4j
public class OutboxListener {

    public static final Supplier<IllegalArgumentException> EMPTY_OUTBOX_EVENT =
            () -> new IllegalArgumentException("Empty outbox event.");

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @EventListener
    public void on(OutboxEvent outboxEvent) {
        log.debug("Receive an outbox event {}.", outboxEvent);
        String eventData = OutboxUtils.toJsonString(objectMapper, outboxEvent).orElseThrow(EMPTY_OUTBOX_EVENT);
        Outbox outbox = new Outbox();
        outbox.setOutboxId(UUID.randomUUID().toString());
        outbox.setEventData(eventData);
        outbox.setOccurredOn(LocalDateTime.now());
        this.outboxRepository.save(outbox);
        log.debug("Successfully saved an outbox event.");
    }

}
