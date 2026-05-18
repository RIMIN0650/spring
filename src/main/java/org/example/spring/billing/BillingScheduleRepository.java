package org.example.spring.billing;

import org.example.spring.billing.model.BillingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingScheduleRepository extends JpaRepository<BillingSchedule, Long> {
    List<BillingSchedule> findByUserId(Long userId);
    BillingSchedule findByOrderId(String orderId);
}
