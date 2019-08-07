package dev.muskrat.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private List<OrderProductDTO> products;

    private String phone;
    private String address;
    private String name;
    private String email;
    private Long shopId;
    private Integer status;
    private LocalDateTime createdTime;

    private String comments;
}
