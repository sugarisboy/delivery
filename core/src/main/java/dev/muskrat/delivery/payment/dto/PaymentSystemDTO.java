package dev.muskrat.delivery.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSystemDTO {

    private Long id;

    private String name;

    private Boolean active;

    private Boolean online;
}
