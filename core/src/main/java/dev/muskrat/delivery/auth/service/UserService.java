package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dao.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> findAll();

    default User findByUsername(String username) {
        return findByEmail(username);
    }

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);
}
