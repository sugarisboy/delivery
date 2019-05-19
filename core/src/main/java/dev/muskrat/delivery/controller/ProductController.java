package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dto.ProductDTO;
import dev.muskrat.delivery.service.ProductServiceImpl;
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
