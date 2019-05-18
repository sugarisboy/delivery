package dev.muskrat.delivery.controller;

import dev.muskrat.delivery.model.Partner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/partner")
public class PartnerController {

    @PostMapping("/register")
    public ResponseEntity<Partner> register(@RequestBody Partner partner) {
        return new ResponseEntity<>(partner, HttpStatus.OK);
    }

}
