package org.example.spring.orders.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Setter
    @ColumnDefault("false")
    private boolean paid; // 결제 유무

    private int paymentPrice; // 총 계산한 금액

    @Setter
    // 실제 결제 후 PG사에서 결제된 결과 가지고 업데이트 할 때 사용
    private String pgPaymentId;

    @OneToMany(mappedBy = "orders")
    private List<OrdersItem> items;

}
