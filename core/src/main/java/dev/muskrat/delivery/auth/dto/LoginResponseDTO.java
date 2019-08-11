package dev.muskrat.delivery.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private String user;
    private String token;
}
