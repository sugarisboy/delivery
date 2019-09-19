package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.dto.UserDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateDTO;
import dev.muskrat.delivery.auth.dto.UserUpdateResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(User user);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    UserDTO findById(Long id, String authorization);

    UserUpdateResponseDTO update(UserUpdateDTO userUpdateDTO);

    default Optional<User> findByUsername(String username) {
        return findByEmail(username);
    }
}
