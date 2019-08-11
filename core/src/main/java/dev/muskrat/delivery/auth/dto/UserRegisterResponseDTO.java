package dev.muskrat.delivery.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponseDTO {

    private Long id;
    private String username;
    private String access;
}
