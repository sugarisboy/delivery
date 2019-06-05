package dev.muskrat.delivery.dto.product;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ProductCreateDTO {

    @NotEmpty
    private String title;

    @Positive
    private Double price;

    private String description;

    @Positive
    private Double value;

    private String quantity;

    @Min(1)
    @Max(7)
    private Long category;

}

