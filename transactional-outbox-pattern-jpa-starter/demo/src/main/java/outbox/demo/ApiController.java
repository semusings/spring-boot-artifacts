package outbox.demo;

import io.github.bhuwanupadhyay.tx.outbox.config.OutboxProperties;
import io.github.bhuwanupadhyay.tx.outbox.domain.Outbox;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxEvent;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxPublisher;
import io.github.bhuwanupadhyay.tx.outbox.domain.OutboxRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ApiController {

    private final OutboxPublisher eventPublisher;
    private final OutboxRepository repository;

    public ApiController(OutboxPublisher eventPublisher, OutboxRepository repository) {
        this.eventPublisher = eventPublisher;
        this.repository = repository;
    }

    @Transactional
    @PostMapping("/outboxes/fire")
    public ResponseEntity<Void> fire() {
        eventPublisher.fire(new DebitedEvent(BigDecimal.TEN));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/outboxes")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Outbox>> getOutboxes() {
        return ResponseEntity.ok(repository.findAll());
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
