package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.product.*;
import dev.muskrat.delivery.exception.EntityNotFoundException;
import dev.muskrat.delivery.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ProductCreateResponseDTO create(
        @Valid @RequestBody ProductCreateDTO productDTO
    ) {
        return productService.create(productDTO);
    }

    @PatchMapping("/update")
    public ProductUpdateResponseDTO update(
        @Valid @RequestBody ProductUpdateDTO productDTO
    ) {
        return productService.update(productDTO);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(@NotNull @PathVariable Long id) {
        return productService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Product not found")
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable Long id) {
        productService.delete(id);
    }

}
