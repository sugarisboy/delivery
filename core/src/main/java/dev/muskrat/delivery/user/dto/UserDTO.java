package dev.muskrat.delivery.user.dto;

import dev.muskrat.delivery.partner.dto.PartnerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long cityId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private PartnerDTO partnerDTO;
}
