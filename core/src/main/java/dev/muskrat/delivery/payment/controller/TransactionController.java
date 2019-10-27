package dev.muskrat.delivery.payment.controller;

import dev.muskrat.delivery.payment.dto.TransactionCreateDTO;
import dev.muskrat.delivery.payment.dto.TransactionDTO;
import dev.muskrat.delivery.payment.dto.TransactionResponseDTO;
import dev.muskrat.delivery.payment.dto.TransactionUpdateDTO;
import dev.muskrat.delivery.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public TransactionResponseDTO create(
        @Valid @RequestBody TransactionCreateDTO dto
    ) {
        return transactionService.create(dto);
    }

    @GetMapping("/get/{transactionId}")
    public TransactionDTO findById(
        @NotNull @PathVariable Long transactionId
    ) {
        return transactionService.findById(transactionId);
    }

    @GetMapping("/get/order/{orderId}")
    public TransactionDTO findByOrderId(
        @NotNull @PathVariable Long orderId
    ) {
        return transactionService.findByOrderId(orderId);
    }

    @PatchMapping("/update")
    public TransactionResponseDTO update(
        @Valid @RequestBody TransactionUpdateDTO dto
    ) {
        return transactionService.update(dto);
    }
}
