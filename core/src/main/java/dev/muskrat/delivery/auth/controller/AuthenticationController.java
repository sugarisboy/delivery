package dev.muskrat.delivery.auth.controller;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.service.AuthorizationService;
import dev.muskrat.delivery.user.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.user.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthorizationService authorizationService;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

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
        @RequestHeader("Key") String key,
        @RequestHeader("Authorization") String authorization
    ) {
        return authorizationService.refresh(key, authorization);
    }

    @GetMapping("/logout")
    @PostMapping("/logout")
    public void logout(
        @RequestHeader("Key") String key,
        @RequestHeader("Authorization") String authorization,
        @RequestParam(value = "type", required = false) String type
    ) {
        User user = jwtAuthorizationToUserConverter.convert(key, authorization);

        if (type == null) {
            authorizationService.logout(user, key);
        } else if (type.equalsIgnoreCase("all")) {
            authorizationService.logoutAll(user);
        } else if (type.equalsIgnoreCase("secure")) {
            authorizationService.logoutSecure(user, key);
        }
    }
}
