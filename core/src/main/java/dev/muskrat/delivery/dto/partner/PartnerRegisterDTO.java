package dev.muskrat.delivery.dto.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRegisterDTO {

    @NotNull
    @Size(min = 1, max = 64)
    private String name;

    @Email
    @NotNull
    private String email;

    @Size(min = 3, max = 15)
    private String phone;

    @NotNull
    private String password;

    @NotNull
    private String passwordRepeat;

}
