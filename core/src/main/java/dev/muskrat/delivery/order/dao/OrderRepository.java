package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findByShop(Shop shop);

    Optional<List<Order>> findByEmail(String email);
}
