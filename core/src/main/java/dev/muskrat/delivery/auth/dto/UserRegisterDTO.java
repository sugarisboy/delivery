package dev.muskrat.delivery.auth.dto;

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
public class UserRegisterDTO {

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 4)
    private String password;

    @NotNull
    private String repeatPassword;

    @NotNull
    private String name;
}
