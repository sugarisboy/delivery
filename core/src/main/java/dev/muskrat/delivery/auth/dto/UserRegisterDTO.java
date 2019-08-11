package dev.muskrat.delivery.auth.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserRegisterDTO {

    @Email
    @NotNull
    private String email;

    @NotNull
    @Min(4)
    private String password;

    @NotNull
    @Min(4)
    private String repeatPassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;
}
