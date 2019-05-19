package dev.muskrat.delivery.service.partner;

import dev.muskrat.delivery.dto.PartnerDTO;
import dev.muskrat.delivery.dto.PartnerRegisterDTO;
import dev.muskrat.delivery.dto.PartnerRegisterResponseDTO;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    PartnerRegisterResponseDTO create(PartnerRegisterDTO partnerRegisterDTO);

    void update(PartnerDTO partnerDTO);

    void delete(PartnerDTO partnerDTO);

    Optional<PartnerDTO> findById(long id);

    List<PartnerDTO> findAll();

}
