package dev.muskrat.delivery.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {

    @Size(min=6, max=11)
    private String phone;

    @NotNull
    private String address;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    private String comment;

    @NotNull
    private Long shopId;

    //@NotEmpty
    private List<OrderProductDTO> products;
}
