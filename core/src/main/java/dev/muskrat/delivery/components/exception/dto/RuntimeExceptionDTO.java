package dev.muskrat.delivery.components.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuntimeExceptionDTO {

    private String message;
    private String method;
    private String clazz;
}
