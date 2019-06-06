package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dto.partner.PartnerDTO;
import org.springframework.stereotype.Component;

@Component
public class PartnerToPartnerDTOConverter implements ObjectConverter<Partner, PartnerDTO> {

    @Override
    public PartnerDTO convert(Partner partner) {
        return PartnerDTO.builder()
            .id(partner.getId())
            .name(partner.getName())
            .build();
    }

}
