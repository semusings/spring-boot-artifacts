package io.github.bhuwanupadhyay.drools.payment;

import java.util.Objects;

public class PaymentActionWrapper {
    private final PaymentAction action;
    private final Payment entity;

    public PaymentActionWrapper(PaymentAction action, Payment entity) {
        this.action = action;
        this.entity = entity;
    }

    public PaymentAction getAction() {
        return action;
    }

    public Payment getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "Action{" +
                "action='" + action + '\'' +
                '}';
    }

    public void rejectIfStatusNotEmpty() {
        if (Objects.nonNull(this.entity.getStatus())) {
            throw new IllegalArgumentException("Payment status should be not initialized");
        }
    }

    public void rejectIfStatusNotMatch(PaymentStatus status) {
        if (Objects.isNull(this.entity.getStatus()) || !Objects.equals(this.entity.getStatus(), status)) {
            throw new IllegalArgumentException("Payment status should be in status: " + status);
        }
    }

    public boolean actionEqualsTo(PaymentAction action) {
        return Objects.equals(this.action, action);
    }

}
