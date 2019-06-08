package dev.muskrat.delivery.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {

    private Long id;

    private List<OrderProductDTO> products;

    private String phone;
    private String address;
    private String name;
    private String email;
    private Long shop;
    private Integer status;

    private String comments;
}
