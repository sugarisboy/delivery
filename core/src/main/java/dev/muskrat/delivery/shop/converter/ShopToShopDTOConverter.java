package dev.muskrat.delivery.shop.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dto.ShopDTO;
import dev.muskrat.delivery.shop.dto.ShopScheduleDTO;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ShopToShopDTOConverter implements ObjectConverter<Shop, ShopDTO> {

    @Override
    public ShopDTO convert(Shop shop) {

        Hibernate.initialize(shop.getOpen());
        List<LocalTime> open = shop.getOpen();
        Hibernate.initialize(shop.getClose());
        List<LocalTime> close = shop.getClose();

        ShopScheduleDTO scheduleDTO =ShopScheduleDTO.builder()
            .id(shop.getId())
            .open(open)
            .close(close)
            .build();

        return ShopDTO.builder()
            .id(shop.getId())
            .name(shop.getName())
            .description(shop.getDescription())
            .freeOrderPrice(shop.getFreeOrderPrice())
            .cityId(shop.getCity().getId())
            .minOrderPrice(shop.getMinOrderPrice())
            .schedule(scheduleDTO)
            .build();
    }
}
