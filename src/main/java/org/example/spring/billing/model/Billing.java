package org.example.spring.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spring.ENUM.BillingStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Billing {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private String billingKey;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private BillingStatus status; // ACTIVE, CANCELLED

}
