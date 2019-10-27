package dev.muskrat.delivery.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private Long paymentsSystemId;
    private Long orderId;
    private Double price;
    private Boolean isPaid;
}
