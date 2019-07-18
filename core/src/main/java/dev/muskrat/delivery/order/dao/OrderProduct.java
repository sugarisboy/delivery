package dev.muskrat.delivery.order.dao;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class OrderProduct {

    private Long productId;

    private Integer count;
}
