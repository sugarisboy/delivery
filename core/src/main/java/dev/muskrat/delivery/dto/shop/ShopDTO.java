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
    private float minOrder;
    private float freeOrder;
    private WeekScheduleDTO schedule;
    private RegionDeliveryDTO region;
}
