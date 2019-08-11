package dev.muskrat.delivery.auth.security;

import dev.muskrat.delivery.auth.converter.UserToJwtIUserConverter;
import dev.muskrat.delivery.auth.dao.AuthorizedUser;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.auth.service.AuthorizedUserService;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final AuthorizedUserService userService;
    private final UserToJwtIUserConverter userToJwtIUserConverter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthorizedUser> authorizedUser = userService.findByEmail(email);

        if (authorizedUser.isEmpty())
            throw new EntityNotFoundException("AuthorizedUser with email " + email + " not found");
        AuthorizedUser user = authorizedUser.get();

        JwtUser jwtUser = userToJwtIUserConverter.convert(user);

        return jwtUser;
    }
}
