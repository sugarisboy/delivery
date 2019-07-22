package dev.muskrat.delivery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageRequestDTO {

    private Long shopId;
    private Long categoryId;
    private String title;
    private Double minPrice;
    private Double maxPrice;
    // todo add city
}
