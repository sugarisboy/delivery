package dev.muskrat.delivery.dto.shop;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class ShopUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    @Size(min = 1, max = 64)
    private String name;

}
