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

    @Size(min = 3, max = 64, message = "Very short or long shop name")
    private String name;
}
