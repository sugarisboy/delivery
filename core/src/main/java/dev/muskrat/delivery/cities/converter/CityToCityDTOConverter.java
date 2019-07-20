package dev.muskrat.delivery.cities.converter;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.cities.dto.CityDTO;
import dev.muskrat.delivery.components.converter.ObjectConverter;
import org.springframework.stereotype.Component;

@Component
public class CityToCityDTOConverter implements ObjectConverter<City, CityDTO> {

    @Override
    public CityDTO convert(City city) {
        return CityDTO.builder()
            .id(city.getId())
            .name(city.getName())
            .build();
    }
}
