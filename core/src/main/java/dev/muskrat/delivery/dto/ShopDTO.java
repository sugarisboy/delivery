package dev.muskrat.delivery.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopDTO {

    private Long id;
    private String name;
}
