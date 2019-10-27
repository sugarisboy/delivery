package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.dao.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
        "SELECT o FROM Order o WHERE" +
            "(:phone is null or o.phone = :phone) and" +
            "(:email is null or o.email = :email) and" +
            "(:city is null or o.city = :city) and" +
            "(:shop is null or o.shop = :shop)"
    )
    Page<Order> findWithFilter(
        @Param("phone") String phone,
        @Param("email") String email,
        @Param("city") City city,
        @Param("shop") Shop shop,
        Pageable pageable
    );

    @Query(
        "SELECT SUM(o.cost) FROM Order o " +
            "WHERE " +
            "o.shop = :shop and " +
            "o.created > :startDate and " +
            "o.created < :endDate and " +
            "o.status > 0"
    )
    Double getProfitByShop(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("shop") Shop shop
    );

    @Query(
        "SELECT COUNT(o) FROM Order o " +
            "WHERE " +
            "o.shop = :shop and " +
            "o.created > :startDate and " +
            "o.created < :endDate and " +
            "o.status > 0"
    )
    Long countOrderByShop(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate,
        @Param("shop") Shop shop
    );

    @Query(
        "SELECT SUM(o.cost) FROM Order o " +
            "WHERE " +
            "o.created > :startDate and " +
            "o.created < :endDate and " +
            "o.status > 0"
    )
    Double getProfit(
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );
}
