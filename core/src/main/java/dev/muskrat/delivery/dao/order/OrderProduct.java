package dev.muskrat.delivery.dao.order;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
public class OrderProduct {

    @NotNull
    private Long productId;

    @NotNull
    private Integer count;
}
