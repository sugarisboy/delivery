package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.Partner;
import dev.muskrat.delivery.dto.PartnerDTO;
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
