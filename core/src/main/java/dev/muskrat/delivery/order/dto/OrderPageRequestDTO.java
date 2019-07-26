package dev.muskrat.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageRequestDTO {

    private Boolean active;
    private Long shopId;
    private Long cityId;

    @Email
    private String email;
    private String phone;
}
