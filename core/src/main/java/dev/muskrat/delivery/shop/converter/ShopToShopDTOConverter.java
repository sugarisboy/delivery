package dev.muskrat.delivery.shop.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.shop.dto.ShopDTO;
import org.springframework.stereotype.Component;

@Component
public class ShopToShopDTOConverter implements ObjectConverter<Shop, ShopDTO> {

    @Override
    public ShopDTO convert(Shop shop) {
        return ShopDTO.builder()
            .id(shop.getId())
            .name(shop.getName())
            .description(shop.getDescription())
            .freeOrderPrice(shop.getFreeOrderPrice())
            .logo(shop.getLogo())
            .cityId(shop.getCity().getId())
            .minOrderPrice(shop.getMinOrderPrice())
            .build();
    }
}
