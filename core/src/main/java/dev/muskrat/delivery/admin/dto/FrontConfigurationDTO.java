package dev.muskrat.delivery.admin.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FrontConfigurationDTO {

    @Value("${application.order.irrevocable-status}")
    private Integer appOrderIrrevocableStatus;

    @Value("${application.jwt.token.expired}")
    private Long jwtTokenExpired;

    @Value("${geocode.country}")
    private String country;
}
