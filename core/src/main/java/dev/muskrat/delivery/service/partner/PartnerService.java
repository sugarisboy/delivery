package dev.muskrat.delivery.service.partner;

import dev.muskrat.delivery.dto.partner.*;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    PartnerRegisterResponseDTO create(PartnerRegisterDTO partnerRegisterDTO);

    PartnerUpdateResponseDTO update(PartnerUpdateDTO partnerDTO);

    void delete(Long id);

    Optional<PartnerDTO> findById(long id);

    Optional<PartnerDTO> findByEmail(String email);

    List<PartnerDTO> findAll();

}
