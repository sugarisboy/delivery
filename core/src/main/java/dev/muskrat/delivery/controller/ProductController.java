package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.product.ProductCreateResponseDTO;
import dev.muskrat.delivery.dto.product.ProductDTO;
import dev.muskrat.delivery.dto.product.ProductDeleteResponseDTO;
import dev.muskrat.delivery.dto.product.ProductUpdateResponseDTO;
import dev.muskrat.delivery.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/create")
    public ProductCreateResponseDTO create(
        @RequestBody ProductDTO productDTO
    ) {
        return productService.create(productDTO);
    }

    @PostMapping("/product/update")
    public ProductUpdateResponseDTO update(
        @RequestBody ProductDTO productDTO
    ) {
        return productService.update(productDTO);
    }

    @PostMapping("/product/delete")
    public ProductDeleteResponseDTO delete(
        @RequestBody ProductDTO productDTO
    ) {

        return productService.delete(productDTO);
    }
}
