package dev.muskrat.delivery.partner.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.partner.dto.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface PartnerService {

    PartnerRegisterResponseDTO create(AuthorizedUser executor);

    boolean isCurrentPartner(Authentication authentication, Long id);
}
