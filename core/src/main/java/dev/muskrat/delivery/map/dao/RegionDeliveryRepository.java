package dev.muskrat.delivery.map.dao;

import dev.muskrat.delivery.shop.dao.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface RegionDeliveryRepository extends JpaRepository<RegionDelivery, Long> {

    Optional<RegionDelivery> findByShop(Shop shop);
}
