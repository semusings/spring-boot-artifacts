package outbox.demo;

import io.github.bhuwanupadhyay.tx.outbox.config.OutboxProperties;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxEvent;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ApiController {

    private final OutboxPublisher eventPublisher;
    private final OutboxProperties properties;

    public ApiController(OutboxPublisher eventPublisher, OutboxProperties properties) {
        this.eventPublisher = eventPublisher;
        this.properties = properties;
    }

    @GetMapping("/outboxes/fire")
    public ResponseEntity<Void> fire() {
        eventPublisher.fire(new DebitedEvent(BigDecimal.TEN));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/outboxes/configs")
    public ResponseEntity<OutboxProperties> getProperties() {
        return ResponseEntity.ok(properties);
    }


    public static class DebitedEvent implements OutboxEvent {

        private final BigDecimal amount;

        public DebitedEvent(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }
}
