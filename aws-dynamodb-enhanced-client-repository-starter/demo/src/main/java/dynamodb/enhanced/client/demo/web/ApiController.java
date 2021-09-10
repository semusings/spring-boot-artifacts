package dynamodb.enhanced.client.demo.web;

import dynamodb.enhanced.client.demo.payment.PaymentRepository;
import dynamodb.enhanced.client.demo.payment.Payment;
import dynamodb.enhanced.client.demo.payment.PaymentStatus;
import io.github.bhuwanupadhyay.aws.dynamodb.data.Filters;
import io.github.bhuwanupadhyay.aws.dynamodb.data.ListPage;
import io.github.bhuwanupadhyay.aws.dynamodb.data.PageQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ApiController {

    private final PaymentRepository repository;

    public ApiController(PaymentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/payments")
    public ResponseEntity<ListPage<Payment>> getPayments(PageQuery pageQuery) {
        return ResponseEntity.ok(repository.scanGrid(Filters.create(), pageQuery));
    }

    @PostMapping("/payments")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest request) {
        Payment payment = new Payment();
        payment.setReference(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.valueOf(request.getStatus()));
        return ResponseEntity.ok(repository.create(payment));
    }

    @PutMapping("/payments/{reference}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("reference") String reference, @RequestBody PaymentRequest request) {
        Payment payment = repository.scanOne(reference);
        payment.setStatus(PaymentStatus.valueOf(request.getStatus()));
        return ResponseEntity.ok(repository.update(payment));
    }

}
