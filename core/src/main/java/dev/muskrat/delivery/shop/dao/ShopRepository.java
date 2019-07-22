package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByName(String name);

    @NotFound(action = NotFoundAction.IGNORE)
    List<Shop> findAll();

    Page<Shop> findAll(Pageable pageable);

    Page<Shop> findAllByCity(City city, Pageable pageable);
}
