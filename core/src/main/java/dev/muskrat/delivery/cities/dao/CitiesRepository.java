package dev.muskrat.delivery.cities.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<City, Long> {

    @Query("SELECT o FROM City o where (o.id = :id) and deleted=0")
    Optional<City> findById(
        @Param("id") Long id
    );

    Optional<City> findByName(String name);
}
