package dev.muskrat.delivery.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopPageRequestDTO {

    @Min(3)
    private String name;

    @Positive
    private Long cityId;

    @Positive
    private Double maxMinOrderPrice;

    @Positive
    private Double maxFreeOrderPrice;

    private String deliveryFor;
}
