package dev.muskrat.delivery.auth.security;

import dev.muskrat.delivery.components.exception.EntityNotFoundException;
import dev.muskrat.delivery.user.converter.UserToJwtIUserConverter;
import dev.muskrat.delivery.user.dao.User;
import dev.muskrat.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserToJwtIUserConverter userToJwtIUserConverter;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> authorizedUser = userRepository.findByEmail(email);

        if (authorizedUser.isEmpty())
            throw new EntityNotFoundException("User with email " + email + " not found");
        User user = authorizedUser.get();

        return userToJwtIUserConverter.convert(user);
    }
}
