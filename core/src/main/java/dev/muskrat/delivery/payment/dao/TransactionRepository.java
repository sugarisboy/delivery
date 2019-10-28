package dev.muskrat.delivery.payment.dao;

import dev.muskrat.delivery.order.dao.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByOrder(Order order);
}
