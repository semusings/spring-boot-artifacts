package io.github.bhuwanupadhyay.tx.outbox.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OutboxEventSource {

    String OUTPUT = "outbox";

    @Output(OUTPUT)
    MessageChannel outbox();

}
