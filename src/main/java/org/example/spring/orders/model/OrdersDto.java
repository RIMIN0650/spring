package org.example.spring.orders.model;

import lombok.Builder;
import lombok.Getter;
import org.example.spring.user.model.User;

import java.util.List;

public class OrdersDto {

    @Builder
    @Getter
    public static class VerifyReq {
        private String paymentId;
    }

    @Builder
    @Getter
    public static class OrdersReq {
        private Integer paymentPrice;
        private List<Long> courseIdxList;

        public Orders toEntity(User user) {
            return Orders.builder()
                    .paid(false)
                    .paymentPrice(paymentPrice)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrdersRes {
        private Long ordersIdx;
        private boolean paid;

        public static OrdersRes from(Orders entity) {
            return OrdersRes.builder()
                    .ordersIdx(entity.getIdx())
                    .paid(entity.isPaid())
                    .build();
        }
    }
}
