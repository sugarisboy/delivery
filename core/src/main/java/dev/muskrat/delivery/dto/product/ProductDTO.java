package dev.muskrat.delivery.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {

    private Long id;
    private String title;
    private Double price;
    private String description;
    private String url;
    private Boolean available;
    private Double value;
    private Integer category;
}
