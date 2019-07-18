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
    private String name;
    private String logo;
    private String description;
    private Double minOrderPrice;
    private Double freeOrderPrice;
    private Long shopId;
}
