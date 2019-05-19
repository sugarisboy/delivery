package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.product.Product;
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
                .availability(product.isAvailable())
                .value(product.getValue())
                .category(product.getCategory())
                .build();
    }
}
