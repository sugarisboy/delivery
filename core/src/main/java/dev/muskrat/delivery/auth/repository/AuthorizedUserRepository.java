package dev.muskrat.delivery.auth.repository;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizedUserRepository extends JpaRepository<AuthorizedUser, Long> {

    Optional<AuthorizedUser> findByUsername(String username);

    Optional<AuthorizedUser> findByEmail(String email);
}
