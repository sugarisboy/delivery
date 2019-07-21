package dev.muskrat.delivery.product.controller;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.product.service.ProductService;
import dev.muskrat.delivery.shop.dto.ShopPageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/get/{id}")
    public ProductDTO findById(@NotNull @PathVariable Long id) {
        return productService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Product not found")
        );
    }

    @DeleteMapping("/{id}")
    public void delete(@NotNull @PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/page")
    public ProductPageDTO page(
        @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable page
    ) {
        return productService.findAll(page);
    }
}
