package dev.muskrat.delivery.user.service;

import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.dto.UserDTO;
import dev.muskrat.delivery.user.dto.UserUpdateDTO;
import dev.muskrat.delivery.user.dto.UserUpdateResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User register(User user);

    List<User> findAll();

    UserDTO findByEmail(String email);

    UserDTO findById(Long id);

    UserUpdateResponseDTO update(UserUpdateDTO userUpdateDTO);

    default UserDTO findByUsername(String username) {
        return findByEmail(username);
    }
}
