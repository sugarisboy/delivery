package dev.muskrat.delivery.partner.controller;

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

    @PostMapping("/register")
    public PartnerRegisterResponseDTO register(
        @Valid @RequestBody PartnerRegisterDTO partnerRegisterDTO
    ) {
        return partnerService.create(partnerRegisterDTO);
    }

    @GetMapping("/{id}")
    public PartnerDTO findById(
        @Positive @PathVariable Long id
    ) {
        return partnerService.findById(id).orElseThrow();
    }

    @PatchMapping
    public PartnerUpdateResponseDTO update(
        @Valid @RequestBody PartnerUpdateDTO partnerUpdateDTO
    ) {
        return partnerService.update(partnerUpdateDTO);
    }

}
