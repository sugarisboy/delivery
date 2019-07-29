package dev.muskrat.delivery.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageRequestDTO {

    @Min(3)
    private String title;

    @Positive
    private Long shopId;

    @Positive
    private Long categoryId;

    @Positive
    private Double minPrice;

    @Positive
    private Double maxPrice;
    // todo add city
}
