package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.shop.*;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import dev.muskrat.delivery.service.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/create")
    public ShopCreateResponseDTO create(
        @Valid @RequestBody ShopCreateDTO shopCreateDTO
    ) {
        return shopService.create(shopCreateDTO);
    }

    @PatchMapping("/update")
    public ShopUpdateResponseDTO update(
        @Valid @RequestBody ShopUpdateDTO shopUpdateDTO
    ) {
        return shopService.update(shopUpdateDTO);
    }

    @GetMapping("/{id}")
    public ShopDTO findById(@NotNull @PathVariable Long id) {
        return shopService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Partner not found")
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable Long id) {
        shopService.delete(id);
    }

}
