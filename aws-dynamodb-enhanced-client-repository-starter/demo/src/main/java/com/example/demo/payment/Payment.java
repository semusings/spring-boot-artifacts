package com.example.demo.payment;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.io.Serializable;
import java.util.Objects;

@DynamoDbBean
public class Payment implements Serializable {

    private String reference;

    private PaymentStatus status;

    public Payment() {
    }

    public Payment(String reference) {
        this.reference = reference;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("reference")
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @DynamoDbAttribute("status")
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
