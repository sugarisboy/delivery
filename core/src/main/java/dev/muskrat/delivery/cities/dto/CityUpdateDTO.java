package dev.muskrat.delivery.cities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Size(min = 3, max = 64, message = "very short or long shop name")
    // remove not null when add new editable field
    private String name;
}
