package dev.muskrat.delivery.dao.order;

import dev.muskrat.delivery.dao.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findByShop(Shop shop);

    Optional<Order> findByEmail(String email);
}
