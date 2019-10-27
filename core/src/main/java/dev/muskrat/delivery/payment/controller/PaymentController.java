package dev.muskrat.delivery.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PaymentController {

    @PostMapping("/success")
    public void successfulPayment() {

    }
}
