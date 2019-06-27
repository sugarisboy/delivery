package dev.muskrat.delivery.converter;

import dev.muskrat.delivery.dao.partner.Partner;
import dev.muskrat.delivery.dto.partner.PartnerRegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class PartnerRegisterDTOToPartnerConverter
    implements ObjectConverter<PartnerRegisterDTO, Partner> {

    @Override
    public Partner convert(PartnerRegisterDTO partnerRegisterDTO) {
        Partner partner = new Partner();
        partner.setEmail(partnerRegisterDTO.getEmail());
        partner.setPassword(partnerRegisterDTO.getPassword());
        return partner;
    }

}
