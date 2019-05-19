package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.shop.ShopCreateResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopDTO;
import dev.muskrat.delivery.dto.shop.ShopDeleteResponseDTO;
import dev.muskrat.delivery.dto.shop.ShopUpdateResponseDTO;
import dev.muskrat.delivery.service.ShopServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopServiceImpl shopService;

    @PostMapping("/shop/create")
    public ShopCreateResponseDTO create(
            @RequestBody ShopDTO shopDTO
    ) {
        return shopService.create(shopDTO);
    }

    @PostMapping("/shop/update")
    public ShopUpdateResponseDTO update(
            @RequestBody ShopDTO shopDTO
    ) {
        return shopService.update(shopDTO);
    }

    @PostMapping("/shop/delete")
    public ShopDeleteResponseDTO delete(
            @RequestBody ShopDTO shopDTO
    ) {
        return shopService.delete(shopDTO);
    }
}
