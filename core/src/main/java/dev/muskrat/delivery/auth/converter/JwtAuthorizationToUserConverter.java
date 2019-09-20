package dev.muskrat.delivery.auth.converter;

import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.repository.UserRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.components.exception.JwtAuthenticationException;
import dev.muskrat.delivery.components.exception.JwtTokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationToUserConverter {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public User convert(String key, String authorization) {
        String resolveToken = jwtTokenProvider.resolveToken(authorization);
        if (resolveToken == null)
            throw new JwtAuthenticationException("Jwt auth exception");

        if (!jwtTokenProvider.validateRefreshToken(key, resolveToken) &&
            !jwtTokenProvider.validateAccessToken(key, resolveToken))
            throw new JwtTokenExpiredException("Token is expired");

        String username = jwtTokenProvider.getUsername(resolveToken);
        if (username == null)
            throw new JwtAuthenticationException("Jwt auth exception");

        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty())
            throw new UsernameNotFoundException("User with " + username + " not found");

        return byUsername.get();
    }
}
