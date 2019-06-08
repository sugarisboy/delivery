package dev.muskrat.delivery.dto.shop;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShopDTO {

    private Long id;
    private String name;
    private String logo;
    private String description;
    private Float minOrder;
    private Float freeOrder;
    private List<WorkDayDTO> schedule;
    private RegionDeliveryDTO region;
    private Boolean visible;
}
