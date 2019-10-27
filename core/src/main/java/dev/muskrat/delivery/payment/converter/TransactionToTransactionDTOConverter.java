package dev.muskrat.delivery.payment.converter;

import dev.muskrat.delivery.components.converter.ObjectConverter;
import dev.muskrat.delivery.order.dao.Order;
import dev.muskrat.delivery.payment.dao.PaymentSystemEntity;
import dev.muskrat.delivery.payment.dao.Transaction;
import dev.muskrat.delivery.payment.dto.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class TransactionToTransactionDTOConverter implements ObjectConverter<Transaction, TransactionDTO> {

    @Override
    public TransactionDTO convert(Transaction transaction) {
        Order order = transaction.getOrder();
        Long orderId = order.getId();

        PaymentSystemEntity system = transaction.getSystem();
        Long systemId = system.getId();

        return TransactionDTO.builder()
            .id(transaction.getId())
            .orderId(orderId)
            .paymentsSystemId(systemId)
            .isPaid(transaction.getPaid())
            .price(transaction.getPrice())
            .build();
    }
}
