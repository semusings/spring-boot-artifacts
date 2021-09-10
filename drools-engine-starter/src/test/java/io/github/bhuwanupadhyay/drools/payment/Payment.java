package io.github.bhuwanupadhyay.drools.payment;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Payment {

    @Id
    private String reference;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment() {
    }

    public Payment(String reference) {
        this.reference = reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(reference, payment.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "reference='" + reference + '\'' +
                ", status=" + status +
                '}';
    }
}

