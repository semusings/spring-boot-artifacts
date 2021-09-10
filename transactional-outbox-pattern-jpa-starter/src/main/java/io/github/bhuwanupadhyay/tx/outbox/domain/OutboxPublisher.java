package io.github.bhuwanupadhyay.tx.outbox.domain;

public interface OutboxPublisher {

    void fire(OutboxEvent outboxEvent);

}
