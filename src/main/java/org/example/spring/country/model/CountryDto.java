package org.example.spring.country.model;

import lombok.Builder;
import lombok.Getter;

public class CountryDto {

    @Builder
    @Getter
    public static class AddCountryReq {
        private String name;

        public Country toEntity() {
            return Country.builder()
                    .name(name)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AddCountryRes {
        private Long id;
        private String name;

        private static CountryDto.AddCountryRes from(Country entity) {
            return CountryDto.AddCountryRes.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build();
        }
    }

}
