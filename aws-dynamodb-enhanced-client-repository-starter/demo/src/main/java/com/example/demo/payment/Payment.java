package com.example.demo.payment;

import io.github.bhuwanupadhyay.aws.dynamodb.data.Identifiable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;
import java.util.Objects;

@DynamoDbBean
public class Payment implements Identifiable, Serializable {

    private String reference;

    private PaymentStatus status;

    public Payment() {
    }

    public Payment(String reference) {
        this.reference = reference;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("Reference")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @DynamoDbAttribute("Status")
    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    @DynamoDbIgnore
    public String getId() {
        return this.reference;
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
