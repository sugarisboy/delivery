package dev.muskrat.delivery.user.service;

import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.user.dto.UserDTO;
import dev.muskrat.delivery.user.dto.UserUpdateDTO;
import dev.muskrat.delivery.user.dto.UserUpdateResponseDTO;

public interface UserService {

    void create(AuthorizedUser authorizedUser);

    UserUpdateResponseDTO update(AuthorizedUser authorizedUser, UserUpdateDTO userUpdateDTO);

    UserDTO get(AuthorizedUser authorizedUser);
}
