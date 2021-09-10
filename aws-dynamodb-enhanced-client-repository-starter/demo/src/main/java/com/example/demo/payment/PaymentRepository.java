package com.example.demo.payment;

import io.github.bhuwanupadhyay.aws.dynamodb.model.DynamoRepository;
import io.github.bhuwanupadhyay.aws.dynamodb.repository.AbstractDynamoRepository;
import org.springframework.stereotype.Repository;

@Repository
@DynamoRepository(tableName = "payments", entityClass = Payment.class)
public class PaymentRepository extends AbstractDynamoRepository<Payment> {
}
