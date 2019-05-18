package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.dto.PartnerDTO;
import dev.muskrat.delivery.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody PartnerDTO partnerDTO) {

        return new ResponseEntity(HttpStatus.OK);
    }

}
