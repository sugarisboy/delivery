package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("SELECT o FROM Shop o where (o.id = :id) and deleted=0")
    Optional<Shop> findById(
        @Param("id") Long id
    );

    Optional<Shop> findByName(String name);

    Page<Shop> findAll(Pageable pageable);

    @Query(
        "SELECT o FROM Shop o WHERE" +
            "(:name is null or o.name like :name) and" +
            "(:city is null or o.city = :city) and" +
            "(:maxMinOrderPrice is null or o.minOrderPrice <= :maxMinOrderPrice) and" +
            "(:maxFreeOrderPrice is null or o.freeOrderPrice <= :maxFreeOrderPrice)"
    )
    Page<Shop> findWithFilter(
        @Param("name") String name,
        @Param("city") City city,
        @Param("maxMinOrderPrice") Double maxMinOrderPrice,
        @Param("maxFreeOrderPrice") Double maxFreeOrderPrice,
        Pageable pageable
    );
}
