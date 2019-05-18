package dev.muskrat.delivery.products;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @JsonView(Product.Details.class)
    @PostMapping("/product/create")
            private ResponseEntity<ProductDTO> create(
            @Validated(Product.New.class) @RequestBody ProductDTO product
    ) {


        return new ResponseEntity<>(service.create(product), HttpStatus.OK);
    }
}
