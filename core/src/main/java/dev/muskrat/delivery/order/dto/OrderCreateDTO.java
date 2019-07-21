package dev.muskrat.delivery.order.dto;

import dev.muskrat.delivery.validations.validators.ValidShop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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

    @NotNull
    @Size(min=6, max=11)
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;


    @NotNull
    @ValidShop
    private Long shopId;

    @Valid
    @NotEmpty
    private List<OrderProductDTO> products;

    private String comment;
}
