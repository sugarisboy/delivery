package dev.muskrat.delivery.partner.controller;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.partner.dto.*;
import dev.muskrat.delivery.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PartnerRegisterResponseDTO register(
        @RequestHeader("Authorization") String authorization
    ) {
        AuthorizedUser authorizedUser = jwtAuthorizationToUserConverter.convert(authorization);
        return partnerService.create(authorizedUser);
    }
}
