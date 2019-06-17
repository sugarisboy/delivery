package dev.muskrat.delivery.dto.mapping;

import dev.muskrat.delivery.validators.ValidPoint;
import dev.muskrat.delivery.validators.ValidShop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionUpdateDTO {

    @ValidShop
    private Long shopId;

    @NotNull
    @ValidPoint
    private List<Double> points;
}
