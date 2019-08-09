package dev.muskrat.delivery.auth.controller;

import dev.muskrat.delivery.auth.dto.AuthRequestDTO;
import dev.muskrat.delivery.auth.models.User;
import dev.muskrat.delivery.auth.security.jwt.JwtPasswordEncoder;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(
        @RequestBody AuthRequestDTO authRequestDTO
    ) {
        try {
            String username = authRequestDTO.getUsername();
            String password = authRequestDTO.getPassword();

            Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = userService.findByUsername(username);
            if (user == null)
                throw new UsernameNotFoundException("User with username " + username + " not found");

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
