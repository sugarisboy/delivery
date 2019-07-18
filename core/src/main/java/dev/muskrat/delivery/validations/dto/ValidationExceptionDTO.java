package dev.muskrat.delivery.validations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationExceptionDTO {

    private Integer id;
    private String message;
    private String field;
}