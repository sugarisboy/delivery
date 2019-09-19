package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.repository.UserRepository;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
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

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtAuthorizationToUserConverter jwtAuthorizationToUserConverter;

    @Override
    public UserRegisterResponseDTO register(UserRegisterDTO userRegisterDTO) {

        String email = userRegisterDTO.getEmail();
        String lastName = userRegisterDTO.getLastName();
        String password = userRegisterDTO.getPassword();
        String firstName = userRegisterDTO.getFirstName();
        String repeatPassword = userRegisterDTO.getRepeatPassword();

        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new JwtAuthenticationException("This email is already taken");
        }

        if (!password.equals(repeatPassword)) {
            throw new JwtAuthenticationException("Password and repeat password not equals");
        }

        User user = new User();

        user.setEmail(email);
        user.setUsername(email);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setFirstName(firstName);

        User registeredUser = userService.register(user);

        String token = jwtTokenProvider.createToken(registeredUser);

        return UserRegisterResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
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

            Optional<User> byUsername = userService.findByUsername(username);
            if (byUsername.isEmpty())
                throw new UsernameNotFoundException("User with username " + username + " not found");
            User user = byUsername.get();

            String token = jwtTokenProvider.createToken(user);

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
        User user = jwtAuthorizationToUserConverter.convert(authorization);
        String token = jwtTokenProvider.refreshToken(user, refresh);

        String username = user.getUsername();

        return UserLoginResponseDTO.builder()
            .username(username)
            .access(token)
            .build();
    }


    @Override
    public boolean isEquals(Authentication authentication, Long authorizedUserId) {
        Optional<User> byId = userRepository.findById(authorizedUserId);
        if (byId.isEmpty())
            throw new EntityNotFoundException("User with id " + authorizedUserId + " not found");
        User user = byId.get();

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String jwtUserEmail = jwtUser.getEmail();

        String authorizedUserEmail = user.getEmail();

        return jwtUserEmail.equalsIgnoreCase(authorizedUserEmail);
    }


}