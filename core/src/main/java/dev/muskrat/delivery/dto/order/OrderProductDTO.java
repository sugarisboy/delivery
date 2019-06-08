package dev.muskrat.delivery.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDTO {

    @NotNull
    private Long product;

    @NotNull
    private Integer count;
}
