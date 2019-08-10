package dev.muskrat.delivery.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
