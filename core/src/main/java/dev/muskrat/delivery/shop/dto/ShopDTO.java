package dev.muskrat.delivery.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

    private Long id;
    private Long cityId;
    private Long partnerId;
    private String name;
    private String description;
    private Double deliveryCost;
    private Double minOrderCost;
    private Double freeDeliveryCost;
    private ShopScheduleDTO schedule;
}
