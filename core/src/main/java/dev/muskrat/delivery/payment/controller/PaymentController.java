package dev.muskrat.delivery.payment.controller;

import dev.muskrat.delivery.payment.dto.PaymentSystemCreateDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemResponseDTO;
import dev.muskrat.delivery.payment.dto.PaymentSystemUpdateDTO;
import dev.muskrat.delivery.payment.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsService paymentsService;

    @PostMapping("/create")
    private PaymentSystemResponseDTO create(
        @Valid @RequestBody PaymentSystemCreateDTO paymentSystemCreateDTO
        ) {
        return paymentsService.create(paymentSystemCreateDTO);
    }

    @GetMapping("/get/{systemId}")
    private PaymentSystemDTO findById(
        @NotNull @PathVariable Long systemId
    ) {
        return paymentsService.findById(systemId);
    }

    @PatchMapping("/update")
    private PaymentSystemResponseDTO update(
        @Valid @RequestBody PaymentSystemUpdateDTO PaymentSystemUpdateDTO
    ) {
        return paymentsService.update(PaymentSystemUpdateDTO);
    }
}
