package dev.muskrat.delivery.map.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RegionPoint {

    private Double x;
    private Double y;
    private Double z = 100D;
}
