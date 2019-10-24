package dev.muskrat.delivery.product.dto;

import dev.muskrat.delivery.validations.validators.ValidShop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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

    @NotNull
    @ValidShop
    private Long shopId;

    @Positive
    private Long category;

}

