package dev.muskrat.delivery.order.dto;

import dev.muskrat.delivery.validations.validators.ValidOrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTO {

    @NotNull
    @ValidOrderProduct
    private Long productId;

    @NotNull
    @Positive
    private Integer count;
}
