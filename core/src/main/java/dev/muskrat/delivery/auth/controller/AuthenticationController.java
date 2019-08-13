package dev.muskrat.delivery.auth.controller;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    public UserRegisterResponseDTO register(
        @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ) {
        return authorizationService.register(userRegisterDTO);
    }

    @PostMapping("/login")
    public UserLoginResponseDTO login(
        @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        return authorizationService.login(userLoginDTO);
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('USER')")
    public UserLoginResponseDTO refresh(
        @RequestHeader("Authorization") String authorization
    ) {
        return authorizationService.refresh(authorization);
    }
}
