package dev.muskrat.delivery.partner.controller;

import dev.muskrat.delivery.partner.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.partner.service.PartnerService;
import dev.muskrat.delivery.user.converter.AuthIdToAuthorizedUserConverter;
import dev.muskrat.delivery.user.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final AuthIdToAuthorizedUserConverter authIdToAuthorizedUserConverter;

    @PostMapping("/create/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PartnerRegisterResponseDTO register(
        @NotNull @PathVariable Long userId
    ) {
        User user = authIdToAuthorizedUserConverter.convert(userId);
        return partnerService.create(user);
    }
}
