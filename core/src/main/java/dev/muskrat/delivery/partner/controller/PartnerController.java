package dev.muskrat.delivery.partner.controller;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.partner.dto.*;
import dev.muskrat.delivery.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
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
    public PartnerRegisterResponseDTO register(
        @Valid @RequestBody PartnerRegisterDTO partnerRegisterDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        AuthorizedUser authorizedUser = jwtAuthorizationToUserConverter.convert(authorization);
        return partnerService.create(authorizedUser, partnerRegisterDTO);
    }

    /*@GetMapping("/{id}")
    public PartnerDTO findById(
        @Positive @PathVariable Long id
    ) {
        return partnerService.findById(id).orElseThrow();
    }*/

    @PatchMapping("/update")
    public PartnerUpdateResponseDTO update(
        @Valid @RequestBody PartnerUpdateDTO partnerUpdateDTO
    ) {
        return partnerService.update(partnerUpdateDTO);
    }

}
