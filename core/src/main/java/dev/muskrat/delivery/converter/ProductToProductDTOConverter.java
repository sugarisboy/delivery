package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.product.Product;
import dev.muskrat.delivery.dto.product.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductDTOConverter implements ObjectConverter<Product, ProductDTO> {


    @Override
    public ProductDTO convert(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .price(product.getPrice())
            .available(product.getAvailable())
            .value(product.getValue())
            .category(product.getCategory().getId())
            .shopId(product.getShop().getId())
            .build();
    }
}
