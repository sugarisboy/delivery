package dev.muskrat.delivery.product.dao;

import dev.muskrat.delivery.shop.dao.Shop;
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
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT o FROM Product o where (o.id = :id) and deleted=0")
    Optional<Product> findById(
        @Param("id") Long id
    );

    @Query(
        "SELECT o FROM Product o WHERE" +
            "(:title is null or o.title like :title) and" +
            "(:category is null or o.category = :category) and" +
            "(:shop is null or o.shop = :shop) and" +
            "(o.price <= :maxPrice and o.price >= :minPrice)"
    )
    Page<Product> findWithFilter(
        @Param("title") String title,
        @Param("shop") Shop shop,
        @Param("category") Category category,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        Pageable pageable
    );
}
