package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByName(String name);

    Page<Shop> findAll(Pageable pageable);

    Page<Shop> findAllByCity(City city, Pageable pageable);
}
