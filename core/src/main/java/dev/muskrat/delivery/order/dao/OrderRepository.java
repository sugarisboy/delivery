package dev.muskrat.delivery.order.dao;

import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.dao.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
        "SELECT o FROM Order o WHERE" +
            "(:phone is null or o.phone = :phone) and" +
            "(:email is null or o.email = :email) and" +
            "(:city is null or o.city = :city) and" +
            "(:shop is null or o.shop = :shop) and" +
            "(:status is null or o.status < :status)"
    )
    Page<Order> findWithFilter(
        @Param("phone") String phone,
        @Param("email") String email,
        @Param("city") City city,
        @Param("shop") Shop shop,
        @Param("status") Integer status,
        Pageable pageable
    );
}
