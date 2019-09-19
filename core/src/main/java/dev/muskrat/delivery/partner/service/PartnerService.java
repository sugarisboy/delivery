package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.partner.dto.*;
import org.springframework.security.core.Authentication;

public interface PartnerService {

    PartnerRegisterResponseDTO create(User executor);

    boolean isCurrentPartner(Authentication authentication, Long id);
}
