package dev.muskrat.delivery.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageRequestDTO {

    @Positive
    private Long cityId;

    private String phone;

    private String name;
}
