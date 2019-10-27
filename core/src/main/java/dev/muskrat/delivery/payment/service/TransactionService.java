package dev.muskrat.delivery.payment.service;

import dev.muskrat.delivery.payment.dto.TransactionCreateDTO;
import dev.muskrat.delivery.payment.dto.TransactionDTO;
import dev.muskrat.delivery.payment.dto.TransactionResponseDTO;
import dev.muskrat.delivery.payment.dto.TransactionUpdateDTO;

public interface TransactionService {
    TransactionResponseDTO create(TransactionCreateDTO transactionCreateDTO);

    TransactionDTO findById(Long transactionId);

    TransactionDTO findByOrderId(Long orderId);

    TransactionResponseDTO update(TransactionUpdateDTO transactionUpdateDTO);
}
