package dev.muskrat.delivery.shop.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopScheduleDTO {

    private Long id;

    private List<LocalTime> open;

    private List<LocalTime> close;
}
