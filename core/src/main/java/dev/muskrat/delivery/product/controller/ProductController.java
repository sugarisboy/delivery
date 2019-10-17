package dev.muskrat.delivery.product.controller;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.product.dto.*;
import dev.muskrat.delivery.product.service.ProductService;
import dev.muskrat.delivery.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ShopService shopService;
    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @shopServiceImpl.isShopOwner(authentication, #productDTO.shopId))")
    public ProductCreateResponseDTO create(
        @Valid @RequestBody ProductCreateDTO productDTO
    ) {
        return productService.create(productDTO);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @productServiceImpl.isProductOwner(authentication, #productDTO.id))")
    public ProductUpdateResponseDTO update(
        @Valid @RequestBody ProductUpdateDTO productDTO
    ) {
        return productService.update(productDTO);
    }

    @GetMapping("/{id}")
    public ProductDTO findById(
        @NotNull @PathVariable Long id
    ) {
        return productService.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Product not found")
        );
    }

    @PostMapping("/page")
    public ProductPageDTO page(
        @Valid @RequestBody(required = false) ProductPageRequestDTO requestDTO,
        @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable page
    ) {
        return productService.findAll(requestDTO, page);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('PARTNER') and @productServiceImpl.isProductOwner(authentication, #id))")
    public void delete(
        @NotNull @PathVariable Long id
    ) {
        productService.delete(id);
    }
}
