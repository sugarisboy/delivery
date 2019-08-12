package dev.muskrat.delivery.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshDTO {

    @NotNull
    private String username;

    @NotNull
    private String token;
}
