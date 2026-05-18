package org.example.spring.billing.model;

import lombok.Builder;
import lombok.Getter;

public class BillingDto {

    @Getter
    public static class BillingKeyDto {
        private String billingKey;
    }

    @Builder
    public static class BillingKeyDtoRes {
        private Long userIdx;
        private String billingKey;
    }


    @Getter
    public class BillingRegisterReq {
        private String billingKey;
        private String customerId;
    }


    @Getter
    public class WebhookPayload {
        private String type;       // "Payment.Paid", "Payment.Failed"
        private String paymentId;
        private String customerId;
    }
}
