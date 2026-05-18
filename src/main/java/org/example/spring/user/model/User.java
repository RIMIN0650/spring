package org.example.spring.user.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String email;
    private String name;
    @Setter
    private String password;

    private boolean enable;
    private String role;

    @Setter
    @Column(name = "billing_key", nullable = true)
    private String billingKey;
}