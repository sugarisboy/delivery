package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.ShopDTO;
import dev.muskrat.delivery.service.ShopServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopServiceImpl service;

    @PostMapping("/shop/create")
    public ResponseEntity create(
            @RequestBody ShopDTO shopDTO
    ) {
        service.create(shopDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/shop/update")
    public ResponseEntity update(
            @RequestBody ShopDTO shopDTO
    ) {
        service.create(shopDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/shop/delete")
    public ResponseEntity delete(
            @RequestBody ShopDTO shopDTO
    ) {
        service.create(shopDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
