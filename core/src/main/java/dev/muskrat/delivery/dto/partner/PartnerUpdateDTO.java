package dev.muskrat.delivery.dto.partner;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class PartnerUpdateDTO {

    @NotNull
    @Positive
    private Long id;

    private String name;

    private String password;

    @Email
    @Nullable
    private String email;

    @Size(min = 3, max = 15)
    private String phone;


}
