package dev.muskrat.delivery.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

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

    private String logo;
    private String description;

    @Positive
    private Float minOrder;

    @Positive
    private Float freeOrder;

    @Size(min = 7, max = 7, message = "Workday should be seven")
    private List<WorkDayDTO> schedule;

    private RegionDeliveryDTO region;

    private Boolean visible;

}
