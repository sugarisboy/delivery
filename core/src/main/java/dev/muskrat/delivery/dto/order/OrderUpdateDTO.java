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

    /**
     * 0 - Processing
     * 1 - Assembly
     * 2 - Delivery
     *
     * 10 - Done
     * 11 - Cancel
     */
    @Positive
    private Integer status;
}
