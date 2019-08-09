package dev.muskrat.delivery.auth.repository;

import dev.muskrat.delivery.auth.models.Role;
import dev.muskrat.delivery.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
