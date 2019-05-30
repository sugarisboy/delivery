package dev.muskrat.delivery.dto.product;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@Builder
public class CategoryDTO {

    @Positive
    private Long id;

    @NotEmpty
    private String title;

}
