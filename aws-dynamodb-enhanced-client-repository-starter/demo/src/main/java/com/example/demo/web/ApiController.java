package com.example.demo.web;

import com.example.demo.payment.Payment;
import com.example.demo.payment.PaymentRepository;
import io.github.bhuwanupadhyay.aws.dynamodb.data.Filters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ApiController {

    private final PaymentRepository repository;

    public ApiController(PaymentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getPayments() {
        return ResponseEntity.ok(repository.scanAll(Filters.create()));
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment request) {
        request.setReference(UUID.randomUUID().toString());
        return ResponseEntity.ok(repository.create(request));
    }

    @PutMapping("/payments/{reference}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("reference") String reference, @RequestBody Payment request) {
        Payment payment = repository.scanOne(reference);
        request.setStatus(payment.getStatus());
        return ResponseEntity.ok(repository.update(payment));
    }

}
