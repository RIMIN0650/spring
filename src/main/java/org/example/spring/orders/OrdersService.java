package org.example.spring.orders;

import lombok.RequiredArgsConstructor;
import org.example.spring.course.CourseRepository;
import org.example.spring.course.model.Course;
import org.example.spring.orders.model.Orders;
import org.example.spring.orders.model.OrdersDto;
import org.example.spring.orders.model.OrdersItem;
import org.example.spring.user.model.AuthUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersItemRepository ordersItemRepository;
    private final CourseRepository courseRepository;

    public OrdersDto.OrdersRes create(AuthUserDetails user, OrdersDto.OrdersReq dto) {
        List<Course> courseList = courseRepository.findAllById(dto.getCourseIdxList());

        Orders orders = ordersRepository.save(dto.toEntity(user.toEntity()));

        for (Course course : courseList) {
            OrdersItem ordersItem = OrdersItem.builder()
                    .course(course)
                    .orders(orders)
                    .build();
            ordersItemRepository.save(ordersItem);
        }

        return OrdersDto.OrdersRes.from(orders);
    }
}
