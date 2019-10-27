package dev.muskrat.delivery.user.repository;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.cities.dao.City;
import dev.muskrat.delivery.shop.dao.Shop;
import dev.muskrat.delivery.user.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Long countByRolesLike(Role role);

    @Query(
        "SELECT o FROM User o WHERE" +
            "(:name is null or o.name like :name) and" +
            "(:phone is null or o.phone like :phone) and" +
            "(:city is null or o.city = :city)"
    )
    Page<User> findWithFilter(
        @Param("city") City city,
        @Param("name") String name,
        @Param("phone") String phone,
        Pageable pageable
    );
}
