package org.example.spring.country;

import lombok.RequiredArgsConstructor;
import org.example.spring.city.CityRepository;
import org.example.spring.country.model.CountryDto;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CountryService {

    public final CountryRepository countryRepository;
    public final CityRepository cityRepository;

    public CountryDto.AddCountryRes createCountry(String countryName) {
        CountryDto.AddCountryReq addCountryReqDto = CountryDto.AddCountryReq.builder()
                .name(countryName)
                .build();

        countryRepository.save(addCountryReqDto.toEntity());

        return CountryDto.AddCountryRes.builder()
                .id(addCountryReqDto.toEntity().getId())
                .name(addCountryReqDto.getName())
                .build();
    }

}
