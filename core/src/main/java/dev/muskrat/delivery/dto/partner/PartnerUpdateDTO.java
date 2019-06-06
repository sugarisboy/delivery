package dev.muskrat.delivery.dto.partner;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class PartnerUpdateDTO {

    private Long id;

    private String name;

    private String password;

    @Email
    @Nullable
    private String email;

    @Size(min = 3, max = 15)
    private String phone;


}
