package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.Shop;
import dev.muskrat.delivery.dto.ShopDTO;
import org.springframework.stereotype.Component;

@Component
public class ShopToShopDTOConverter implements ObjectConverter<Shop, ShopDTO> {

    @Override
    public ShopDTO convert(Shop shop) {
        return ShopDTO.builder()
                .id(shop.getId())
                .name(shop.getName())
                .build();
    }
}
