package dev.muskrat.delivery.dto;

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

    private boolean availability;

    private Double value;

}
