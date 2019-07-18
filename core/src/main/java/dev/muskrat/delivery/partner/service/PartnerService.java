package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.partner.dto.*;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    PartnerRegisterResponseDTO create(PartnerRegisterDTO partnerRegisterDTO);

    PartnerRegisterResponseDTO createByOrder(Order order);

    PartnerUpdateResponseDTO update(PartnerUpdateDTO partnerDTO);

    void delete(Long id);

    Optional<PartnerDTO> findById(long id);

    Optional<PartnerDTO> findByEmail(String email);

    List<PartnerDTO> findAll();

}
