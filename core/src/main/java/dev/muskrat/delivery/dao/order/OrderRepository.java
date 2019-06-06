package dev.muskrat.delivery.dao.order;

import dev.muskrat.delivery.dao.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByShop(Shop shop);
}
