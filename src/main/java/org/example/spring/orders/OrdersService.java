package org.example.spring.orders;



import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.ToNumberPolicy;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.Payment;
import io.portone.sdk.server.payment.PaymentClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spring.course.CourseRepository;
import org.example.spring.course.model.Course;
import org.example.spring.orders.model.Orders;
import org.example.spring.orders.model.OrdersDto;
import org.example.spring.orders.model.OrdersItem;
import org.example.spring.user.model.AuthUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrdersItemRepository ordersItemRepository;
    private final CourseRepository courseRepository;
    private final PaymentClient pg;

    @Transactional
    public void verify(AuthUserDetails authUserDetails, OrdersDto.VerifyReq dto) {
        CompletableFuture<Payment> completableFuture = pg.getPayment(dto.getPayementId());
        Payment payment = completableFuture.join();

        if (payment instanceof PaidPayment paidPayment) {
            Map<String, Object> customData = new GsonBuilder()
                    .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                    .create().fromJson(paidPayment.getCustomData(), Map.class);

            Long ordersIdx = Long.parseLong(customData.get("ordersIdx").toString());
            Orders orders = ordersRepository.findById(ordersIdx).orElseThrow();

            int totalPrice = orders.getItems().stream()
                    .map(OrdersItem::getCourse)
                    .mapToInt(Course::getPrice)
                    .sum();

            if (paidPayment.getAmount().getTotal() == totalPrice) {
                orders.setPaid(true);
                orders.setPgPaymentId(dto.getPayementId());
                ordersRepository.save(orders);
            }



        }

    }



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
