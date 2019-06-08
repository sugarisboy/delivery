package dev.muskrat.delivery.dto.order;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class OrderUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @Positive
    private Integer status;
}
