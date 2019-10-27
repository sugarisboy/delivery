package dev.muskrat.delivery.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

    private Long shopId;

    private List<Double> abscissa;

    private List<Double> ordinate;

    private Double deliveryCost;

    private Double minOrderCost;

    private Double freeDeliveryCost;
}
