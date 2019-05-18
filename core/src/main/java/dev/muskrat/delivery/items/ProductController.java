package dev.muskrat.delivery.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/product/create")
    private void create(
            @RequestBody Product product
    ) {

    }
}
