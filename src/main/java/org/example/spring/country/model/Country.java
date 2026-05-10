package org.example.spring.country.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.spring.city.model.City;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "country")
    private List<City> cityList = new ArrayList<>();

}
