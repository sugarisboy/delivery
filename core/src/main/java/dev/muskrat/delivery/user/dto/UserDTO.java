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

    private Long id;
    private Long cityId;
    private String name;
    private String email;
    private String phone;
    private PartnerDTO partnerDTO;
}
