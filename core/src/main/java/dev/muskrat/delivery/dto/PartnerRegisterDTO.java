package dev.muskrat.delivery.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PartnerRegisterDTO {

    private String name;
    private String email;
    private String phone;
    private String password;
    private String passwordRepeat;

}
