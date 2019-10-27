package dev.muskrat.delivery.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSystemUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    private String name;

    private Boolean active;

    private Boolean online;
}
