package org.example.spring.city.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring.country.model.Country;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Country country;
}
