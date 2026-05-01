package org.example.spring.orders;

import org.example.spring.orders.model.OrdersItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersItemRepository extends JpaRepository <OrdersItem, Long> {
}
