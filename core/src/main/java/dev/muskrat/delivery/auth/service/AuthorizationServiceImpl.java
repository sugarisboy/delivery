package dev.muskrat.delivery.auth.service;

import dev.muskrat.delivery.auth.dto.UserLoginDTO;
import dev.muskrat.delivery.auth.dto.UserLoginResponseDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterDTO;
import dev.muskrat.delivery.auth.dto.UserRegisterResponseDTO;
import dev.muskrat.delivery.auth.security.jwt.JwtToken;
import dev.muskrat.delivery.auth.security.jwt.JwtTokenProvider;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.auth.security.jwt.TokenStore;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.components.exception.JwtAuthenticationException;
import dev.muskrat.delivery.user.converter.JwtAuthorizationToUserConverter;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import dev.muskrat.delivery.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final TokenStore tokenStore;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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

        JwtToken token = jwtTokenProvider.generateJwtToken(registeredUser);

        return UserRegisterResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .key(token.getKey())
            .access(token.getAccess())
            .refresh(token.getRefresh())
            .build();
    }

    @Override
    public UserLoginResponseDTO login(UserLoginDTO userLoginDTO) {
        try {
            String username = userLoginDTO.getUsername();
            String password = userLoginDTO.getPassword();

            Optional<User> byUsername = userService.findByUsername(username);
            if (byUsername.isEmpty())
                throw new AccessDeniedException("Password or username not valid");
            User user = byUsername.get();

            if (!bCryptPasswordEncoder.matches(password, user.getPassword()))
                throw new AccessDeniedException("Password or username not valid");

            Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            JwtToken token = jwtTokenProvider.generateJwtToken(user);

            return UserLoginResponseDTO.builder()
                .username(user.getUsername())
                .key(token.getKey())
                .access(token.getAccess())
                .refresh(token.getRefresh())
                .build();

        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Override
    public UserLoginResponseDTO refresh(String key, String authorization) {
        String refresh = jwtTokenProvider.resolveToken(authorization);
        User user = jwtAuthorizationToUserConverter.convert(key, authorization);
        JwtToken token = jwtTokenProvider.updateJwtToken(user, refresh);

        return UserLoginResponseDTO.builder()
            .username(user.getUsername())
            .key(token.getKey())
            .access(token.getAccess())
            .refresh(token.getRefresh())
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

    @Override
    public void logout(User user, String key) {
        tokenStore.removeTokenByKey(user.getId(), key);
    }

    @Override
    public void logoutAll(User user) {
        tokenStore.clearTokensByUserId(user.getId());
    }

    @Override
    public void logoutSecure(User user, String key) {
        tokenStore.clearExceptByKey(user.getId(), key);
    }
}