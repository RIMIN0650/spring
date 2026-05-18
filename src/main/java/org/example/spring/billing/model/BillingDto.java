package org.example.spring.billing.model;

import lombok.Builder;
import lombok.Data;
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
    public static class BillingRegisterReq {
        private String billingKey;
        private String customerId;
    }


    @Getter
    public static class WebhookPayload {
        private String type;
        private String paymentId;
        private String customerId;
    }

    @Data
    @Getter
    @Builder
    public static class BillingScheduleRes {
        private Boolean success;
        private String paymentId;
        private String scheduledTime;
        private String message;
    }
}
