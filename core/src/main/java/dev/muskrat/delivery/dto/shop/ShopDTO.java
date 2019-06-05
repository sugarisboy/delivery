package dev.muskrat.delivery.dto.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDTO {

    private Long id;
    private String name;
    private String logo;
    private String description;
    private Double minOrderPrice;
    private Double freeOrderPrice;
    private RegionDeliveryDTO region;
    private Boolean visible;
}
