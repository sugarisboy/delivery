package dev.muskrat.delivery.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

    @Size(min=6, max=10)
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String name;

    @Email
    private String email;

    @NotNull
    private String comments;

    @NotNull
    private Long shop;

    @NotEmpty
    private List<OrderProductDTO> products;
}
