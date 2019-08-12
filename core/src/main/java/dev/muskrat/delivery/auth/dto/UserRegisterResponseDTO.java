package dev.muskrat.delivery.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponseDTO {

    private Long id;
    private String username;
    private String access;
}
