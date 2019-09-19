package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import org.springframework.security.core.Authentication;

public interface AuthorizationService {

    UserRegisterResponseDTO register(UserRegisterDTO userRegisterDTO);

    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);

    UserLoginResponseDTO refresh(String refresh);

    boolean isEquals(Authentication authentication, Long authorizedUserId);
}
