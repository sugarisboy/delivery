package dev.muskrat.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private List<OrderProductDTO> products;

    @Positive
    private Double costAndDelivery;
}
