package dev.muskrat.delivery.user.converter;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDTOConverter implements ObjectConverter<User, UserDTO> {
    @Override
    public UserDTO convert(User user) {
        City city = user.getCity();
        Long cityId = city != null ? city.getId() : null;

        return UserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .cityId(cityId)
            .build();
    }
}
