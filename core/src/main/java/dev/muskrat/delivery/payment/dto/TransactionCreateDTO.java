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
public class TransactionCreateDTO {

    @NotNull
    @Positive
    private Long paymentsSystemId;

    @NotNull
    @Positive
    private Long orderId;

    @NotNull
    @Positive
    private Double price;
}
