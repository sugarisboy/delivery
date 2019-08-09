package dev.muskrat.delivery.auth.converter;

import dev.muskrat.delivery.auth.models.Status;
import dev.muskrat.delivery.auth.models.User;
import dev.muskrat.delivery.auth.security.jwt.JwtUser;
import dev.muskrat.delivery.components.converter.ObjectConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserToJwtIUserConverter implements ObjectConverter<User, JwtUser> {

    @Override
    public JwtUser convert(User user) {
        return new JwtUser(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getPassword(),
            user.getEmail(),
            user.getStatus().equals(Status.ACTIVE),
            user.getUpdated(),
            mapRoleToGrantedAuthority(user)
        );
    }

    private List<GrantedAuthority> mapRoleToGrantedAuthority(User user) {
        return user.getRoles().stream()
            .map(role ->
                new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
}
