package dev.muskrat.delivery.auth.security;

import dev.muskrat.delivery.auth.converter.UserToJwtIUserConverter;
import dev.muskrat.delivery.auth.dao.User;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.auth.service.UserService;
import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserToJwtIUserConverter userToJwtIUserConverter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if (user == null)
            throw new EntityNotFoundException("User with email " + email + " not found");

        JwtUser jwtUser = userToJwtIUserConverter.convert(user);

        return jwtUser;
    }
}
