package org.example.spring.city.model;

import lombok.Builder;
import lombok.Getter;
import org.example.spring.country.model.Country;

public class CityDto {

    @Getter
    public static class AddCityReq {
        private String name;
        private Country country;

        public City toEntity() {
            return City.builder()
                    .name(name)
                    .country(country)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AddCityRes {
        private Long id;
        private String name;
        private Country country;

        private static AddCityRes from(City entity) {
            return AddCityRes.builder()
                    .name(entity.getName())
                    .country(entity.getCountry())
                    .build();
        }
    }


}
