package dev.muskrat.delivery.partner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerDTO {

    private Long id;

    private String name;

    private String password;

    private String email;

    private String phone;

}
