package dev.muskrat.delivery.payment.service;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.order.dao.OrderRepository;
import dev.muskrat.delivery.payment.converter.TransactionToTransactionDTOConverter;
import dev.muskrat.delivery.payment.dao.PaymentSystemEntity;
import dev.muskrat.delivery.payment.dao.PaymentSystemRepository;
import dev.muskrat.delivery.payment.dao.Transaction;
import dev.muskrat.delivery.payment.dao.TransactionRepository;
import dev.muskrat.delivery.payment.dto.TransactionCreateDTO;
import dev.muskrat.delivery.payment.dto.TransactionDTO;
import dev.muskrat.delivery.payment.dto.TransactionResponseDTO;
import dev.muskrat.delivery.payment.dto.TransactionUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PaymentSystemRepository paymentSystemRepository;
    private final OrderRepository orderRepository;

    private final TransactionToTransactionDTOConverter transactionToTransactionDTOConverter;

    @Override
    public TransactionResponseDTO create(TransactionCreateDTO transactionCreateDTO) {
        Long systemId = transactionCreateDTO.getPaymentsSystemId();
        Long orderId = transactionCreateDTO.getOrderId();

        PaymentSystemEntity paymentSystem = paymentSystemRepository.findById(systemId).orElseThrow(
            () -> new EntityNotFoundException("Payment system with id " + systemId + "not found")
        );

        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException("Order with id " + orderId + "not found")
        );

        Transaction transaction = new Transaction();
        transaction.setPaid(false);
        transaction.setSystem(paymentSystem);
        transaction.setOrder(order);
        transaction.setPrice(transactionCreateDTO.getPrice()); // ???

        Transaction save = transactionRepository.save(transaction);
        Long transactionId = save.getId();

        return TransactionResponseDTO.builder()
            .id(transactionId)
            .build();
    }

    @Override
    public TransactionDTO findById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
            () -> new EntityNotFoundException("Transaction with id " + transactionId + "not found")
        );

        return transactionToTransactionDTOConverter.convert(transaction);
    }

    @Override
    public TransactionDTO findByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new EntityNotFoundException("Order with id " + orderId + "not found")
        );

        Transaction transaction = order.getTransaction();

        return transactionToTransactionDTOConverter.convert(transaction);
    }

    @Override
    public TransactionResponseDTO update(TransactionUpdateDTO dto) {
        Long transactionId = dto.getId();

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
            () -> new EntityNotFoundException("Transaction with id " + transactionId + "not found")
        );

        if (dto.getIsPaid() != null) {
            transaction.setPaid(dto.getIsPaid());
        }

        if (dto.getPrice() != null) {
            transaction.setPrice(dto.getPrice());
        }

        transactionRepository.save(transaction);

        return TransactionResponseDTO.builder()
            .id(transactionId)
            .build();
    }
}
