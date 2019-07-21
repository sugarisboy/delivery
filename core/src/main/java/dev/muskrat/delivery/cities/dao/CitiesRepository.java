package dev.muskrat.delivery.cities.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {

    Optional<City> findById(Long id);
    Optional<City> findByName(String name);
}
