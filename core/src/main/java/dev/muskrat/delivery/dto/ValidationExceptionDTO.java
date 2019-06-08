package dev.muskrat.delivery.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationExceptionDTO {

    private Integer id;
    private String message;
    private String field;
}