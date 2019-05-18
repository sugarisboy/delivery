package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.ProductDTO;
import dev.muskrat.delivery.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping("/product/create")
    private ResponseEntity create(
            @RequestBody ProductDTO productDTO
    ) {
        productService.create(productDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/product/update")
    private ResponseEntity update(
            @RequestBody ProductDTO productDTO
    ) {
        productService.update(productDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/product/delete")
    private ResponseEntity delete(
            @RequestBody ProductDTO productDTO
    ) {
        productService.delete(productDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
