package dev.muskrat.delivery.partner.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.partner.dao.Partner;
import dev.muskrat.delivery.partner.dto.PartnerRegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class PartnerRegisterDTOToPartnerConverter
    implements ObjectConverter<PartnerRegisterDTO, Partner> {

    @Override
    public Partner convert(PartnerRegisterDTO partnerRegisterDTO) {
        Partner partner = new Partner();
        return partner;
    }

}
