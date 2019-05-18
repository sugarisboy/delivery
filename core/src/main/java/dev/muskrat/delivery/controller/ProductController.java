package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dto.ProductDTO;
import dev.muskrat.delivery.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl service;

    @PostMapping("/product/create")
    private ResponseEntity<Product> create(
        @RequestBody ProductDTO productDTO
    ) {


        return new ResponseEntity<>(service.create(productDTO), HttpStatus.OK);
    }
}
