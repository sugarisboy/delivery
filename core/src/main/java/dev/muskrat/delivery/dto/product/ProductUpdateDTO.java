package dev.muskrat.delivery.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
