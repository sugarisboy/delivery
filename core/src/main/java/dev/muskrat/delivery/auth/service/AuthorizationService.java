package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;

public interface AuthorizationService {

    UserRegisterResponseDTO register(UserRegisterDTO userRegisterDTO);

    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);

    UserLoginResponseDTO refresh(String refresh);
}
