package org.example.spring.billing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_schedules")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String billingKey;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String scheduledTime; // ISO 8601 형식

    @Column(nullable = false)
    private String status; // SCHEDULED, COMPLETED, FAILED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}