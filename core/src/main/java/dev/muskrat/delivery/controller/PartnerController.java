package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dao.Partner;
import dev.muskrat.delivery.dto.PartnerDTO;
import dev.muskrat.delivery.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/register")
    public ResponseEntity<Partner> register(@RequestBody PartnerDTO partnerDTO) {
    }

}
