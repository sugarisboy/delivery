package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.Product;
import dev.muskrat.delivery.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductDTOConverter implements ObjectConverter<Product, ProductDTO> {


    @Override
    public ProductDTO convert(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .url(product.getImageUrl())
                .price(product.getPrice())
                .availability(true) //Оно не видит поче-муто метода getAvailabity//
                .value(product.getValue())
                .build();
    }
}
