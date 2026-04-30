package org.example.spring.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository OrdersRepository;

}
