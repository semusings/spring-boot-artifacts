package io.github.bhuwanupadhyay.tx.outbox.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = Outbox.TABLE_NAME)
@Getter
@Setter
public class Outbox implements Serializable {

    public static final String TABLE_NAME = "outbox";
    public static final String OUTBOX_ID = "outbox_id";
    public static final String SORT_KEY = "occurred_on";
    public static final String EVENT_DATA = "event_data";

    @Id
    @Column(name = OUTBOX_ID)
    private String outboxId;
    @Basic(fetch = FetchType.LAZY)
    @ToString.Exclude
    @Column(name = EVENT_DATA, nullable = false, length = 10485760)
    private String eventData;
    @Column(name = SORT_KEY, nullable = false)
    private LocalDateTime occurredOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outbox outbox = (Outbox) o;
        return Objects.equals(outboxId, outbox.outboxId) && Objects.equals(occurredOn, outbox.occurredOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(outboxId, occurredOn);
    }

    @Override
    public String toString() {
        return "Outbox{" +
                "outboxId='" + outboxId + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}
