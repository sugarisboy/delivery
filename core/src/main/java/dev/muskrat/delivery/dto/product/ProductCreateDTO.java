package dev.muskrat.delivery.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

