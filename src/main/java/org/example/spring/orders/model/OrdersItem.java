package org.example.spring.orders.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring.course.model.Course;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name="orders_idx")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name="course_idx") // 후에 나의 상품으로 변경
    private Course course;

}
