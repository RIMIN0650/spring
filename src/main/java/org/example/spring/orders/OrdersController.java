package org.example.spring.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrdersController {

    private final OrdersService ordersService;



}
