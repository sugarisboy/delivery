package dev.muskrat.delivery.user.repository;

import dev.muskrat.delivery.auth.dao.Role;
import dev.muskrat.delivery.user.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Long countByRolesLike(Role role);
}
