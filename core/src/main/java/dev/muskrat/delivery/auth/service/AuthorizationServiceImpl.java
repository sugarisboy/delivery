package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.components.exception.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AuthorizedUserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @Override
    public UserRegisterResponseDTO register(UserRegisterDTO userRegisterDTO) {

        String email = userRegisterDTO.getEmail();
        String lastName = userRegisterDTO.getLastName();
        String password = userRegisterDTO.getPassword();
        String firstName = userRegisterDTO.getFirstName();
        String repeatPassword = userRegisterDTO.getRepeatPassword();

        Optional<AuthorizedUser> byEmail = userService.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new JwtAuthenticationException("This email is already taken");
        }

        if (!password.equals(repeatPassword)) {
            throw new JwtAuthenticationException("Password and repeat password not equals");
        }

        AuthorizedUser authorizedUser = new AuthorizedUser();

        authorizedUser.setEmail(email);
        authorizedUser.setUsername(email);
        authorizedUser.setLastName(lastName);
        authorizedUser.setPassword(password);
        authorizedUser.setFirstName(firstName);

        AuthorizedUser registeredUser = userService.register(authorizedUser);
        String token = jwtTokenProvider.createToken(registeredUser);

        return UserRegisterResponseDTO.builder()
            .id(authorizedUser.getId())
            .username(authorizedUser.getUsername())
            .access(token)
            .build();
    }

    @Override
    public UserLoginResponseDTO login(UserLoginDTO userLoginDTO) {
        try {
            String username = userLoginDTO.getUsername();
            String password = userLoginDTO.getPassword();

            Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Optional<AuthorizedUser> byUsername = userService.findByUsername(username);
            if (byUsername.isEmpty())
                throw new UsernameNotFoundException("AuthorizedUser with username " + username + " not found");
            AuthorizedUser authorizedUser = byUsername.get();

            String token = jwtTokenProvider.createToken(authorizedUser);

            return UserLoginResponseDTO.builder()
                .username(username)
                .access(token)
                .build();

        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public UserLoginResponseDTO refresh(String authorization) {

        String resolveToken = jwtTokenProvider.resolveToken(authorization);
        String refresh = jwtTokenProvider.getRefresh(resolveToken);
        AuthorizedUser authorizedUser = jwtAuthorizationToUserConverter.convert(authorization);
        String token = jwtTokenProvider.refreshToken(authorizedUser, refresh);

        String username = authorizedUser.getUsername();

        return UserLoginResponseDTO.builder()
            .username(username)
            .access(token)
            .build();
    }
}