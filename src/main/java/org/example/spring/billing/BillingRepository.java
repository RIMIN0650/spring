package org.example.spring.billing;

import org.example.spring.billing.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    Billing findByCustomerId(String customerId);

}
