package io.github.bhuwanupadhyay.tx.outbox.service;

import io.github.bhuwanupadhyay.tx.outbox.config.OutboxEventSource;
import io.github.bhuwanupadhyay.tx.outbox.config.OutboxProperties;
import io.github.bhuwanupadhyay.tx.outbox.domain.Outbox;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {

    private final OutboxEventSource eventSource;
    private final OutboxRepository outboxRepository;
    private final OutboxProperties properties;

    @Transactional
    public void checkout(PollerInfo info) {
        PageRequest request = PageRequest.of(0, properties.getMaxEventsPerPolling());

        List<Result> results = outboxRepository.findAllByOrderByOccurredOnAsc(request).stream()
                .map(this::asyncSend)
                .map(CompletableFuture::join)
                .collect(toList());

        List<Outbox> sentOutboxes = results.stream().filter(Result::isSucceed).map(Result::getOutbox).collect(toList());

        info.addCount(sentOutboxes.size());

        outboxRepository.deleteInBatch(sentOutboxes);

        boolean isNotEmpty = !results.isEmpty();

        info.setNextRun(isNotEmpty);
    }

    private CompletableFuture<Result> asyncSend(Outbox outbox) {
        return CompletableFuture.supplyAsync(() -> sendToQueue(outbox));
    }

    private Result sendToQueue(Outbox outbox) {
        try {
            Message<String> message = MessageBuilder.withPayload(outbox.getEventData()).build();
            boolean ack = eventSource.outbox().send(message);
            return new Result(outbox, ack);
        } catch (Exception e) {
            log.error("Error on writing queue {}", outbox, e);
            return new Result(outbox, false);
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class Result {
        private final Outbox outbox;
        private final boolean succeed;
    }
}
