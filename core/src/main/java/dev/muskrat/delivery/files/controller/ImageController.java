package dev.muskrat.delivery.files.controller;

import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import dev.muskrat.delivery.product.service.ProductService;
import dev.muskrat.delivery.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ProductService productService;
    private final ShopService shopService;

    @PostMapping("/product/{productId}")
    public FileStorageUploadDTO updateImgProduct(
        @RequestParam("img") MultipartFile img,
        @PathVariable("productId") Long productId
    ) {
        return productService.updateImg(img, productId);
    }

    @PostMapping("/shop/{shopId}")
    public FileStorageUploadDTO updateImgShop(
        @RequestParam("img") MultipartFile img,
        @PathVariable("shopId") Long shopId
    ) {
        return shopService.updateImg(img, shopId);
    }
}
