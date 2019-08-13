package dev.muskrat.delivery.components.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAccessDeniedExceptionDTO {

    private String clazz;
    private String method;
    private String message;
}
