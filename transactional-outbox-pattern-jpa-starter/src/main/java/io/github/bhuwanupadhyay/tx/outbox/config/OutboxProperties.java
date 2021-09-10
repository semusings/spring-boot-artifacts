package io.github.bhuwanupadhyay.tx.outbox.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "boot.outbox")
public class OutboxProperties {

    private int maxEventsPerPolling = 20;
    private long pollingIntervalInMilliseconds = 10000;
}
