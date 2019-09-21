package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.user.dao.User;
import org.springframework.security.core.Authentication;

public interface AuthorizationService {

    UserRegisterResponseDTO register(UserRegisterDTO userRegisterDTO);

    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);

    UserLoginResponseDTO refresh(String key, String refresh);

    boolean isEquals(Authentication authentication, Long authorizedUserId);

    void logout(User user, String key);

    void logoutAll(User user);

    void logoutSecure(User user, String key);
}
