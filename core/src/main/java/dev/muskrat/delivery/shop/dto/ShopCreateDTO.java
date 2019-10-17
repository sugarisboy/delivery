package dev.muskrat.delivery.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateDTO {

    @NotNull
    @Size(min = 1, max = 64)
    private String name;

    @Positive
    private Long partnerId;

    @NotNull
    @Positive
    private Long cityId;

    @NotNull
    @Size(min = 3)
    private String address;
}
