package dev.muskrat.delivery.shop.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.partner.dao.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("SELECT o FROM Shop o where (o.id = :id) and deleted=0")
    Optional<Shop> findById(
        @Param("id") Long id
    );

    Optional<Shop> findByName(String name);

    @EntityGraph(attributePaths = { "products" }, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Shop> findAll();

    Page<Shop> findAll(Pageable pageable);

    @Query(
        "SELECT o FROM Shop o WHERE" +
            "(:name is null or o.name like :name) and" +
            "(:city is null or o.city = :city)"
    )
    Page<Shop> findWithFilter(
        @Param("name") String name,
        @Param("city") City city,
        Pageable pageable
    );

    List<Shop> findAllByPartner(Partner partner);
}
