package dev.muskrat.delivery.shop.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.map.dao.RegionDelivery;
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

        RegionDelivery region = shop.getRegion();

        return ShopDTO.builder()
            .id(shop.getId())
            .name(shop.getName())
            .description(shop.getDescription())
            .deliveryCost(region.getDeliveryCost())
            .minOrderCost(region.getMinOrderCost())
            .freeDeliveryCost(region.getFreeDeliveryCost())
            .cityId(shop.getCity().getId())
            .schedule(scheduleDTO)
            .partnerId(shop.getPartner().getId())
            .build();
    }
}
