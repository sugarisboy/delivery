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
public class PaymentSystemCreateDTO {

    @NotNull
    private String name;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean online;
}
