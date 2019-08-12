package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.partner.dto.*;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    PartnerRegisterResponseDTO create(AuthorizedUser executor, PartnerRegisterDTO partnerRegisterDTO);

    PartnerUpdateResponseDTO update(PartnerUpdateDTO partnerDTO);

    List<PartnerDTO> findAll();

}
