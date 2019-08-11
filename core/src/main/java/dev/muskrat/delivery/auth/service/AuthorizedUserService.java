package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;

import java.util.List;
import java.util.Optional;

public interface AuthorizedUserService {

    AuthorizedUser register(AuthorizedUser user);

    List<AuthorizedUser> findAll();

    Optional<AuthorizedUser> findByEmail(String email);

    AuthorizedUser findById(Long id);

    void updateRefresh(AuthorizedUser user, String refresh);

    void delete(Long id);

    default Optional<AuthorizedUser> findByUsername(String username) {
        return findByEmail(username);
    }
}
