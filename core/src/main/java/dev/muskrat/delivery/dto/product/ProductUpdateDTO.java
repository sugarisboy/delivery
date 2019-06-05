package dev.muskrat.delivery.dto.product;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ProductUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    private String title;

    private Double price;

    private String description;

    private Double value;

    private String quantity;

    @Min(1)
    @Max(7)
    private Long category;

}
