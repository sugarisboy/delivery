package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dto.PartnerRegisterDTO;
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
