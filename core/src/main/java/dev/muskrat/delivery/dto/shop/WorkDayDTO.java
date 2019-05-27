package dev.muskrat.delivery.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class WorkDayDTO {

    private final LocalTime open;
    private final LocalTime close;
}
