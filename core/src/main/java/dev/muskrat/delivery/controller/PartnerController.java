package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.PartnerRegisterDTO;
import dev.muskrat.delivery.dto.PartnerRegisterResponseDTO;
import dev.muskrat.delivery.service.partner.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/register")
    public PartnerRegisterResponseDTO register(
        @RequestBody PartnerRegisterDTO partnerRegisterDTO
    ) {
        return partnerService.create(partnerRegisterDTO);
    }

}
