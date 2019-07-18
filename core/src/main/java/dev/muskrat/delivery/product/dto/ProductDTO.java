package dev.muskrat.delivery.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

    private Long id;

    private String title;

    private Double price;

    private String description;

    private Boolean available;

    private Double value;

    private Long category;

    private Long shopId;

}
