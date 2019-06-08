package dev.muskrat.delivery.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopScheduleUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @NotEmpty
    @Size(min = 7, max = 7)
    private List<LocalTime> openTimeList;

    @NotEmpty
    @Size(min = 7, max = 7)
    private List<LocalTime> closeTimeList;
}
