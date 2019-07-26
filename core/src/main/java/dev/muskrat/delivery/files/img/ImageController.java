package dev.muskrat.delivery.files.img;

import dev.muskrat.delivery.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/img")
@RequiredArgsConstructor
public class ImageController {

    private final ProductService productService;

    @PostMapping("/product/{productId}")
    public void updateImgProduct(
        @RequestBody MultipartFile img,
        @PathVariable Long productId
    ) {
        productService.updateImg(img, productId);
    }
}
