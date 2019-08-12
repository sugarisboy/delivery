package dev.muskrat.delivery.partner.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dto.PartnerDTO;
import org.springframework.stereotype.Component;

@Component
public class PartnerToPartnerDTOConverter implements ObjectConverter<Partner, PartnerDTO> {

    @Override
    public PartnerDTO convert(Partner partner) {
        return PartnerDTO.builder()
            .id(partner.getId())
            .build();
    }
}
