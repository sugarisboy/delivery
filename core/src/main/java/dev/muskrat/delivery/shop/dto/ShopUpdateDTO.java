package dev.muskrat.delivery.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @Size(min = 1, max = 64)
    private String name;

    private Long cityId;

    private String description;

    @Size(min = 3)
    private String address;

    @Positive
    private Double minOrderCost;

    @Positive
    private Double deliveryCost;

    @Positive
    private Double freeDeliveryCost;
}
