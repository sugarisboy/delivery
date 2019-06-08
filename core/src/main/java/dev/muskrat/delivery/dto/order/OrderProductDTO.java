package dev.muskrat.delivery.dto.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {

    private Long product;
    private Integer count;
}
