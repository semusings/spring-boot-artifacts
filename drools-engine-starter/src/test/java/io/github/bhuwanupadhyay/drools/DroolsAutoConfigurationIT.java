package io.github.bhuwanupadhyay.drools;

import io.github.bhuwanupadhyay.drools.payment.Payment;
import io.github.bhuwanupadhyay.drools.payment.PaymentAction;
import io.github.bhuwanupadhyay.drools.payment.PaymentService;
import io.github.bhuwanupadhyay.drools.payment.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("it")
@SpringBootTest(classes = TestApp.class)
public class DroolsAutoConfigurationIT {

    @Autowired
    private PaymentService paymentService;

    @Test
    void whenReviewThenStatusShouldInitial() {

        String reference = paymentService.create();

        paymentService.workflow(reference, PaymentAction.REVIEW);

        Payment payment = paymentService.getByReference(reference);

        assertEquals(PaymentStatus.INITIAL, payment.getStatus());

    }

    @Test
    void whenApproveThenStatusShouldApproved() {

        String reference = initialPayment();

        paymentService.workflow(reference, PaymentAction.APPROVE);

        Payment payment = paymentService.getByReference(reference);

        assertEquals(PaymentStatus.APPROVED, payment.getStatus());

    }


    @Test
    void whenRejectThenStatusShouldRejected() {

        String reference = initialPayment();

        paymentService.workflow(reference, PaymentAction.REJECT);

        Payment payment = paymentService.getByReference(reference);

        assertEquals(PaymentStatus.REJECTED, payment.getStatus());

    }


    private String initialPayment() {
        String reference = paymentService.create();
        paymentService.workflow(reference, PaymentAction.REVIEW);
        return reference;
    }

}
