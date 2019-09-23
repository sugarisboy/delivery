package dev.muskrat.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageRequestDTO {

    private Boolean active;

    @Positive
    private Long userId;

    @Positive
    private Long shopId;

    @Positive
    private Long cityId;

    @Email
    private String email;

    @Size(min=6, max=11)
    private String phone;
}
