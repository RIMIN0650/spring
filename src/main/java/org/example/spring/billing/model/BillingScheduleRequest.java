package org.example.spring.billing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingScheduleRequest {
    private String billingKey;      // 빌링키
    private String orderId;         // 주문 ID
    private String orderName;       // 주문명
    private Integer amount;         // 결제 금액
    private String timeToPay;
}