package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.user.dao.User;
import org.springframework.security.core.Authentication;

public interface PartnerService {

    PartnerRegisterResponseDTO create(User user);

    boolean isCurrentPartner(Authentication authentication, Long id);
}
