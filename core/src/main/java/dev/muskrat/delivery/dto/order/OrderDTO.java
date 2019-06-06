package dev.muskrat.delivery.dto.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {

    private List<OrderProductDTO> products;

    /**
     * user auth identification
     * use user token or phone, address, name, email, etc..
     */
    private String phone;
    private String address;
    private String name;
    private String email;
    private String token;

    private String comments;
}
