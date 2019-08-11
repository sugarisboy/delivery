package dev.muskrat.delivery.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRefreshDTO {

    @NotNull
    private String username;

    @NotNull
    private String token;
}
