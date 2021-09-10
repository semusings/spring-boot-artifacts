package io.github.bhuwanupadhyay.drools.payment;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private KieContainer kieContainer;
    @Autowired
    private PaymentRepository paymentRepository;

    public String create() {
        return paymentRepository.save(new Payment(UUID.randomUUID().toString())).getReference();
    }

    public void workflow(String reference, PaymentAction action) {
        Payment entity = paymentRepository.getById(reference);
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(new PaymentActionWrapper(action, entity));
        kieSession.fireAllRules();
        kieSession.dispose();
        paymentRepository.save(entity);
    }

    public Payment getByReference(String reference) {
        return paymentRepository.getById(reference);
    }

}
